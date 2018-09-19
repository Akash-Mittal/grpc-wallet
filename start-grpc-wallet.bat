call  src/main/resources/docker/mysql/start.bat
call gradlew.bat build
docker build -t grpc-dev-wallet -f src/main/resources/docker/grpc-wallet/Dockerfile . 
docker run --detach --name=bp-dev-grpc-wallet -p 80:80 grpc-dev-wallet

java -Dwallet.user=2 -Dwallet.request=2 -Dwallet.round=2 -jar build/libs/grpc-wallet.jar
