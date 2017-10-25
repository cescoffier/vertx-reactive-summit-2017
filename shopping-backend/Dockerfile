FROM java:8-alpine
EXPOSE 8080
COPY target/*.jar /vertx/
WORKDIR /vertx
CMD java -jar *.jar -Dvertx.cacheDirBase=/tmp