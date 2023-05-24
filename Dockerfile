FROM openjdk:17-jdk
WORKDIR /app
COPY target/dogtorHouseApp-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "dogtorHouseApp-0.0.1-SNAPSHOT.jar"]