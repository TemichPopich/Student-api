FROM openjdk:22

ARG APPLICATION_JAR=*.jar

COPY ${APPLICATION_JAR} application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]

EXPOSE 8080