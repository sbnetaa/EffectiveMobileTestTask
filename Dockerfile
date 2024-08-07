FROM openjdk:22-jdk

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]