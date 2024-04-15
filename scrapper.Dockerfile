FROM amazoncorretto:21

COPY scrapper/target/scrapper.jar scrapper.jar
EXPOSE 8080

CMD ["java", "-jar", "scrapper.jar"]
