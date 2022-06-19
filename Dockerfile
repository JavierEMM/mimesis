FROM openjdk:17.0.2-jdk
VOLUME /tmp
EXPOSE 8085
ADD ./target/mimesis-0.0.1-SNAPSHOT.jar mimesis.jar
ENTRYPOINT ["java","-jar","mimesis.jar"]
