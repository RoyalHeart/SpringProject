FROM openjdk:8
COPY ./target/ /app
WORKDIR /app
EXPOSE 8083
CMD ["java" ,"-jar", "./demo-0.11.0.jar", "--spring.profiles.active=prod"]
