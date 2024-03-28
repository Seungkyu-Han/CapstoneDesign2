# docker 컨테이너 중지 및 이미지 삭제
sudo docker stop capstone-dev
sudo docker rm capstone-dev
sudo docker image prune -f

#최신버전 pull 후에 빌드
git pull origin backend/dev
./gradlew clean build -x test

# docker 실행
sudo docker build -t capstone:dev .
sudo docker run --name capstone-dev -d -p 8080:8080 --rm capstone:dev
