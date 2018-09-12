FROM openjdk:8
 
COPY build/lobs/grpc-wallet.jar /app/grpc-wallet.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/grpc-wallet.jar"]
 

