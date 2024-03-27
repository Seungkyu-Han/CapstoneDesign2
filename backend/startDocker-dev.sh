# docker 컨테이너 중지 및 이미지 삭제
sudo docker rm shared-routine-dev
sudo docker rmi prune

#최신버전 pull 후에 빌드
git pull origin dev
./gradlew clean build -x test

# docker 실행
sudo docker build -t shared-routine:dev .
sudo docker run --name shared-routine-dev -d -p 8080:8080 --rm shared-routine:dev
