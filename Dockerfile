FROM openjdk:15-jdk-slim
ENV JAVA_OPTS="-Xms256m -Xmx4096m -Djava.security.egd=file:/dev/./urandom"
COPY /clean-me-up-rest/target/clean-me-up-rest-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
