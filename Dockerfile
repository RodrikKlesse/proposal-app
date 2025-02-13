FROM openjdk:17

COPY target/proposal-app-0.0.1-SNAPSHOT.jar proposal-app.jar

COPY wait-for-it.sh wait-for-it.sh

RUN chmod +x wait-for-it.sh

ENTRYPOINT ["./wait-for-it.sh", "rabbit-mq:5672", "--", "java", "-Duser.language=pt", "-Duser.country=BR", "-jar", "proposal-app.jar"]