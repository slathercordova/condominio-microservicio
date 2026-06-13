# 📦 Imagen base con Java 17 (ligera)
FROM eclipse-temurin:17-jdk-alpine

# 📁 Carpeta interna del contenedor
WORKDIR /app

# 📥 Copiamos el .jar generado por Maven al contenedor
# (Debe existir previamente con mvn clean package)
COPY target/*.jar app.jar

# 🌐 Exponemos el puerto en el que corre Spring Boot
EXPOSE 8761

# 🚀 Comando que se ejecuta al iniciar el contenedor
ENTRYPOINT ["java","-jar","app.jar"]