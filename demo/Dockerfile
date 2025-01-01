FROM openjdk:11-jdk

WORKDIR /app

COPY . .

ARG JAR_FILE=/build/libs/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} walletty.jar

ENTRYPOINT ["java", "-jar", "walletty.jar"]