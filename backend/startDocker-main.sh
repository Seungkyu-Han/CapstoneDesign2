# docker 컨테이너 중지 및 이미지 삭제
sudo docker rm shared-routine-main
sudo docker rmi prune

#최신버전 pull 후에 빌드
git pull origin main
./gradlew clean build -x test

# docker 실행
sudo docker build -t shared-routine:main .
sudo docker run --name shared-routine-main -d -p 8080:8080 --rm shared-routine:main
