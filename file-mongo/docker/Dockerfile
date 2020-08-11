FROM  java:8
VOLUME  /tmp
ADD file-mongo-1.0.0.jar filemongo.jar
EXPOSE 8088
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/filemongo.jar"]