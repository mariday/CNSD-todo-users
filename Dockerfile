FROM openjdk:12-alpine
COPY target/users-0.0.1-SNAPSHOT.jar /todo-users.jar
EXPOSE 8082
CMD ["java", "-jar", "/todo-users.jar"]