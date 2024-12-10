FROM node:lts AS nodejs

FROM gradle:jdk21 AS build
COPY --from=nodejs . .
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Final Stage
FROM amazoncorretto:21-alpine AS final
WORKDIR /app
COPY --from=build /app/tasket-server/build/libs/*-all.jar ./app.jar
COPY --from=build /app/tasket-app/build/dist/productionExecutable ./static/

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
