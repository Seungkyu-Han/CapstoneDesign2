# docker 컨테이너 중지 및 이미지 삭제
sudo docker stop capstone
sudo docker rm capstone
sudo docker stop redis
sudo docker rm redis

#최신버전 pull 후에 빌드
git pull origin main
./gradlew clean build -x test

# docker 실행
sudo docker build -t capstone .
sudo docker-compose up -d
sudo docker image prune -f