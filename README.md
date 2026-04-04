# 🎓 University Management System

Актуальная документация проекта находится в папке [`docs/`](./docs/).

## 📚 Документация

- Главная страница документации: [`docs/README.md`](./docs/README.md)
- Архитектура: [`docs/architecture/overview.md`](./docs/architecture/overview.md)
- REST API: [`docs/api/rest-api-reference.md`](./docs/api/rest-api-reference.md)
- Админ‑панель: [`docs/guides/ADMIN_PANEL_GUIDE.md`](./docs/guides/ADMIN_PANEL_GUIDE.md)
- Email (SMTP / уведомления): [`docs/guides/EMAIL_AND_ADMIN_GUIDE.md`](./docs/guides/EMAIL_AND_ADMIN_GUIDE.md)

## 🚀 Быстрый старт (Windows)

```powershell
cd C:\jakarta\university
.\mvnw.cmd clean package -DskipTests
java --enable-native-access=ALL-UNNAMED -jar target\university-1.0-SNAPSHOT.jar
```

Открыть в браузере:
- http://localhost:8080

## 🔐 Тестовые аккаунты

Смотри: [`docs/README.md`](./docs/README.md#-тестовые-аккаунты)
