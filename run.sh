#!/bin/bash
cd C:\jakarta\university
export JAVA_HOME="C:\Program Files\Java\jdk-24"
export PATH="$JAVA_HOME\bin:$PATH"

echo "========================================="
echo "Система управления университетом"
echo "========================================="
echo ""
echo "Запуск приложения на порту 8080..."
echo ""
echo "🌐 Главная страница: http://localhost:8080/"
echo "📖 API документация: http://localhost:8080/api-docs"
echo "📚 REST API: http://localhost:8080/api/main"
echo ""
echo "========================================="
echo ""

# Запускаем приложение через Spring Boot Maven плагин
./mvnw spring-boot:run

