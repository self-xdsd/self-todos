FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} self-todos.jar
ENTRYPOINT ["java","-jar","/self-todos.jar"]
