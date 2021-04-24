FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 7106
ADD target/*.jar flight-book-service-0.2.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /flight-book-service-0.2.jar" ]