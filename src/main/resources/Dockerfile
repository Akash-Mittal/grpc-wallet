FROM openjdk:8

MAINTAINER Akash Mittal "mail.akash.on@gmail.com"
 
COPY build/libs/grpc-wallet.jar /app/grpc-wallet.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/grpc-wallet.jar"]
 


docker run --detach --name=bp-mysql --env="MYSQL_ROOT_PASSWORD=mypassword" --publish 6603:3306 mysql
docker run -p 80:80 a7e6e580c634