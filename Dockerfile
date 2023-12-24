FROM openjdk:17

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src
COPY gradlew /app/
COPY gradle /app/gradle
COPY build/libs/recipify-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["sh", "-c", "java -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]