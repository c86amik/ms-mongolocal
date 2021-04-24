FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 7105
ADD target/*.jar flight-search-service-0.2.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /flight-search-service-0.2.jar" ]