FROM openjdk:8-jre-alpine

COPY app.jar /app.jar
COPY key/ /key

EXPOSE 8080

ENTRYPOINT ["/usr/bin/java", "-jar", "-Dserver.port=8080", "-Dendpoint-url=", "-Dheaders.Authentication=value", "-Dheaders.Authentication2=value2", "/app.jar"]