# ---- stage 1: build the jar ----
#image with the full java 21 jdk
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# copy the Maven wrapper + pom first, download dependencies
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw dependency:go-offline -q

# now copy the source and build
COPY src src
#builds the jar, while skipping the test during image build
RUN ./mvnw package -DskipTests -q

# ---- stage 2: run it ----
#starts a fresh, smaller image with only the java runtime
#no compiler, no maven, no source code, to keep it all lighter
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]