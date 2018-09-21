FROM openjdk:8

MAINTAINER Akash Mittal "mail.akash.on@gmail.com"
 
COPY build/libs/grpc-wallet.jar /app/grpc-wallet.jar

ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/app/grpc-wallet.jar"]

EXPOSE 1234

