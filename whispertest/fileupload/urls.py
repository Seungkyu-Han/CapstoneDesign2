from django.urls import path
from . import views

urlpatterns = [
    path('', views.post_list, name='post_list'),
    path('whisper', views.upload_and_transcribe, name='upload_and_transcribe'),
    path('clova', views.upload_and_clovatranscribe, name='upload_and_clovatranscribe'),
]