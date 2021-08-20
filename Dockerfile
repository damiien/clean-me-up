#FROM maven:3.6.3-jdk-11-slim AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY clean-me-up-support /workspace/clean-me-up-support
#COPY clean-me-up-rest /workspace/clean-me-up-rest
#RUN mvn -B -f pom.xml clean package -DskipTests
#
#FROM openjdk:11-jdk-slim
#COPY --from=build /workspace/clean-me-up-rest/target/clean-me-up-rest-1.0.0-SNAPSHOT.jar app.jar
#EXPOSE 8080
#ENV JAVA_OPTS="-Xms256m -Xmx4096m -Djava.security.egd=file:/dev/./urandom"
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]

FROM openjdk:11-jdk-slim
ENV JAVA_OPTS="-Xms256m -Xmx4096m -Djava.security.egd=file:/dev/./urandom"
COPY clean-me-up-rest/target/clean-me-up-rest-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
