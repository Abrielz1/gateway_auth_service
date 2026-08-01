FROM amazoncorretto:21-alpine
LABEL authors="Abriel"
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY target/gateway-auth-service-0.0.1.jar app.jar
RUN chown appuser:appgroup app.jar
USER appuser
CMD ["java","-jar","app.jar"]