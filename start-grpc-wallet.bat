call gradlew.bat build -x test
docker build --tag grpc-dev-wallet:latest --file src/main/resources/docker/grpc-wallet/Dockerfile  --rm=true . 
docker rm -f bp-dev-grpc-wallet
docker run --detach --name=bp-dev-grpc-wallet --publish 1234:1234 -d grpc-dev-wallet:latest

