FROM openjdk:21-slim
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test
RUN mkdir -p build/libs/logs
RUN chmod -R 777 build/libs/logs