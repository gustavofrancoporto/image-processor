FROM eclipse-temurin:21

ADD image-processor-rest-api.jar image-processor-rest-api.jar

RUN mkdir ./jwt-key

RUN openssl genpkey -out ./jwt-key/app.key -algorithm RSA -pkeyopt rsa_keygen_bits:2048

RUN openssl rsa -in ./jwt-key/app.key -pubout -out ./jwt-key/app.pub

ENV JWT_KEY_FOLDER=/jwt-key

ENTRYPOINT ["java","-jar","image-processor-rest-api.jar"]

EXPOSE 8080