# docker 컨테이너 중지 및 이미지 삭제
sudo docker stop capstone
sudo docker rm capstone
sudo docker stop redis
sudo docker rm redis
sudo docker image prune -f