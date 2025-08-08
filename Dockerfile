FROM openjdk:24-jdk-slim
WORKDIR /app
COPY target/*.jar /app/Electronic_Store.jar
EXPOSE 9090
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/Electronic_Store.jar"]