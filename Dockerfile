FROM openjdk:10-jre

WORKDIR /usr/src/app

COPY target/pdfsigner-*-SNAPSHOT.jar /usr/src/app
CMD java $JAVA_OPTS -cp pdfsigner-*-SNAPSHOT.jar co.teebly.signature.WebServer
