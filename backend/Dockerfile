FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/capstoneDesign-0.0.1-SNAPSHOT.war

WORKDIR /capstone

COPY ${JAR_FILE} capstoneDesign.jar

EXPOSE 8080

CMD ["java", "-jar", "capstoneDesign.jar"]