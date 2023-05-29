FROM openjdk:17-jdk
WORKDIR /app
COPY dogtorHouseApp-0.0.1-SNAPSHOT.jar /app
EXPOSE 80
CMD ["java", "-jar", "dogtorHouseApp-0.0.1-SNAPSHOT.jar"]