FROM amazoncorretto:21

COPY bot/target/bot.jar bot.jar
EXPOSE 8090

CMD ["java", "-jar", "bot.jar"]
