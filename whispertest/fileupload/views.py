from django.shortcuts import render
from django.http import JsonResponse
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from .forms import UploadFileForm
from .models import FileUpload
from openai import OpenAI
import os
from django.conf import settings
import logging
import requests
import json
from django.shortcuts import redirect
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt


secrets_file_path = os.path.join(settings.BASE_DIR, 'secrets.json')
with open(secrets_file_path) as f:
  secrets = json.load(f)

SECRET_KEY = secrets["SECRET_KEY"]
API_KEY = secrets["API_KEY"]

class ClovaSpeechClient:
    # Clova Speech invoke URL
    invoke_url = 'https://clovaspeech-gw.ncloud.com/external/v1/7421/f5e06aecb198b9141483f5c41f10d801eb612d8c808ae261c7e6402e9d935bbf'
    # Clova Speech secret key
    secret = SECRET_KEY

    def req_url(self, url, completion, callback=None, userdata=None, forbiddens=None, boostings=None, wordAlignment=True, fullText=True, diarization=None, sed=None):
        request_body = {
            'url': url,
            'language': 'ko-KR',
            'completion': completion,
            'callback': callback,
            'userdata': userdata,
            'wordAlignment': wordAlignment,
            'fullText': fullText,
            'forbiddens': forbiddens,
            'boostings': boostings,
            'diarization': diarization,
            'sed': sed,
        }
        headers = {
            'Accept': 'application/json;UTF-8',
            'Content-Type': 'application/json;UTF-8',
            'X-CLOVASPEECH-API-KEY': self.secret
        }
        return requests.post(headers=headers,
                             url=self.invoke_url + '/recognizer/url',
                             data=json.dumps(request_body).encode('UTF-8'))

    def req_object_storage(self, data_key, completion, callback=None, userdata=None, forbiddens=None, boostings=None,
                           wordAlignment=True, fullText=True, diarization=None, sed=None):
        request_body = {
            'dataKey': data_key,
            'language': 'ko-KR',
            'completion': completion,
            'callback': callback,
            'userdata': userdata,
            'wordAlignment': wordAlignment,
            'fullText': fullText,
            'forbiddens': forbiddens,
            'boostings': boostings,
            'diarization': diarization,
            'sed': sed,
        }
        headers = {
            'Accept': 'application/json;UTF-8',
            'Content-Type': 'application/json;UTF-8',
            'X-CLOVASPEECH-API-KEY': self.secret
        }
        return requests.post(headers=headers,
                             url=self.invoke_url + '/recognizer/object-storage',
                             data=json.dumps(request_body).encode('UTF-8'))

    def req_upload(self, file, completion, callback=None, userdata=None, forbiddens=None, boostings=None,
                   wordAlignment=True, fullText=True, diarization=None, sed=None):
        request_body = {
            'language': 'ko-KR',
            'completion': completion,
            'callback': callback,
            'userdata': userdata,
            'wordAlignment': wordAlignment,
            'fullText': fullText,
            'forbiddens': forbiddens,
            'boostings': boostings,
            'diarization': diarization,
            'sed': sed,
        }
        headers = {
            'Accept': 'application/json;UTF-8',
            'X-CLOVASPEECH-API-KEY': self.secret
        }
        print(json.dumps(request_body, ensure_ascii=False).encode('UTF-8'))
        files = {
            'media': open(file, 'rb'),
            'params': (None, json.dumps(request_body, ensure_ascii=False).encode('UTF-8'), 'application/json')
        }
        response = requests.post(headers=headers, url=self.invoke_url + '/recognizer/upload', files=files)
        return response
    

@csrf_exempt
def upload_and_transcribe(request):
    logger = logging.getLogger(__name__)
    client = OpenAI(api_key=API_KEY)
    if request.method == 'POST':
        form = UploadFileForm(request.POST, request.FILES)
        if form.is_valid():
            # 업로드된 파일을 임시 저장
            file = request.FILES['file']
            file_name = default_storage.save(os.path.join('temp', file.name), ContentFile(file.read()))
            temp_file_path = os.path.join(settings.MEDIA_ROOT, file_name)
            
            try:
                # OpenAI API 호출
                with open(temp_file_path, 'rb') as file_to_transcribe:
                    transcript = client.audio.transcriptions.create(
                        file=file_to_transcribe,
                        model="whisper-1",
                        language="ko",
                        response_format="text",
                        temperature=0.0,
                    )

                print(transcript)   

                # DB에 파일과 변환된 텍스트 저장
                new_file = FileUpload(audiofile=file, transcribed_text=transcript)
                new_file.save()

                try:
                    # 저장할 텍스트 파일의 전체 경로 생성
                    save_path = os.path.join(settings.MEDIA_ROOT, 'transcribed_texts')
                    os.makedirs(save_path, exist_ok=True)  # 폴더가 없다면 생성

                    filename = os.path.splitext(file.name)[0] + '_transcribed.txt'
                    file_path = os.path.join(save_path, filename)
                    logger.info(f"Saving transcribed text to {file_path}")

                    # 텍스트 파일로 저장
                    with open(file_path, 'w', encoding='utf-8') as text_file:
                        text_file.write(transcript)

                except Exception as e:
                    logger.error(f"Error saving transcribed text: {e}")


            except Exception as e:
                # 예외 발생 시 에러 메시지 반환
                return JsonResponse({'status': 'error', 'message': str(e)})
            finally:
                # 처리가 완료되면, 임시로 저장된 파일을 삭제
                default_storage.delete(file_name)

            return HttpResponse(transcript, content_type="text/plain")
    else:
        form = UploadFileForm()
    return render(request, 'fileupload/whisper_file_upload.html', {'form': form})


@csrf_exempt
def upload_and_clovatranscribe(request):
    logger = logging.getLogger(__name__)
    if request.method == 'POST':
        form = UploadFileForm(request.POST, request.FILES)
        if form.is_valid():
            # 업로드된 파일을 임시 저장
            files = request.FILES['file']
            file_name = default_storage.save(os.path.join('temp', files.name), ContentFile(files.read()))
            temp_file_path = os.path.join(settings.MEDIA_ROOT, file_name)
            
            try:
                # Clova Speech API 호출
                with open(temp_file_path, 'rb') as file_to_transcribe:
                    # 파일 시스템에 저장된 파일의 경로를 사용
                    res = ClovaSpeechClient().req_upload(file=temp_file_path, completion='sync')
                    res_json = res.json()  # 응답을 JSON 형태로 파싱
                    text_result = res_json.get('text', '')  # 'text' 필드에서 텍스트 추출, 없으면 빈 문자열 반환
                
                print(text_result)

                # DB에 파일과 변환된 텍스트 저장
                new_file = FileUpload(audiofile=files, transcribed_text=text_result)
                new_file.save()

                try:
                    # 저장할 텍스트 파일의 전체 경로 생성
                    save_path = os.path.join(settings.MEDIA_ROOT, 'transcribed_texts')
                    os.makedirs(save_path, exist_ok=True)  # 폴더가 없다면 생성

                    filename = os.path.splitext(files.name)[0] + '_transcribed.txt'
                    file_path = os.path.join(save_path, filename)
                    logger.info(f"Saving transcribed text to {file_path}")

                    # 텍스트 파일로 저장
                    with open(file_path, 'w', encoding='utf-8') as text_file:
                        text_file.write(text_result)

                except Exception as e:
                    logger.error(f"Error saving transcribed text: {e}")

            except Exception as e:
                # 예외 발생 시 에러 메시지 반환
                return JsonResponse({'status': 'error', 'message': str(e)})
            finally:
                # 처리가 완료되면, 임시로 저장된 파일을 삭제
                default_storage.delete(file_name)

            return HttpResponse(text_result, content_type="text/plain")
        
    else:
        form = UploadFileForm()
    return render(request, 'fileupload/clova_file_upload.html', {'form': form})


def post_list(request):
    return render(request, 'fileupload/post_list.html', {})



