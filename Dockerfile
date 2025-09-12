FROM maven AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /build/target/*.jar idmarket.jar
EXPOSE 8080
CMD ["java", "-jar", "idmarket.jar"]