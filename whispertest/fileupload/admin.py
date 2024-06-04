from django.contrib import admin
from .models import FileUpload  # 모델이 위치한 경로에 따라 수정해야 할 수 있습니다.

admin.site.register(FileUpload)