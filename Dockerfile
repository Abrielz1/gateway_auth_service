FROM amazoncorretto:21-al2-full
LABEL authors="Abriel"
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY target/gateway_auth_service-0.0.1-SNAPSHOT.jar app.jar
RUN chown appuser:appgroup app.jar
USER appuser
CMD ["java","-jar","app.jar"]