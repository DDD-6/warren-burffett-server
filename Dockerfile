FROM openjdk:11
ARG JAR_FILE=target/*.jar
RUN chmod u+x build.sh
CMD ./build.sh
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]