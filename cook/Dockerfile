# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# ✅ Copy ไฟล์ .jasper เข้าไปใน image
COPY jasper/ /app/jasper/


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
