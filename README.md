![Bot](https://github.com/Zer-01/java_course_2/actions/workflows/bot.yml/badge.svg)
![Scrapper](https://github.com/Zer-01/java_course_2/actions/workflows/scrapper.yml/badge.svg)

# Link Tracker

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 21` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.

### Переменные окружения bot:
- `TELEGRAM_TOKEN` - токен telegram api (обязательная)
- `KAFKA_ENABLED` - используется для включения kafka в модуле(`true`/`false`), `true` - по умолчанию
- `UPDATES_TOPIC` - название топика с сообщениями об обновлении ссылок
- `DLQ_TOPIC_SUFFIX` - суффикс dlq топика(`_dlq` - по умолчанию)
- `DLQ_TOPIC_PARTITIONS` - количество партиций dlq топика
- `DLQ_REPLICAS` - количество реплик dlq топика
- `BOOTSTRAP_SERVERS` - адрес bootstrap сервера
- `GROUP_ID` - kafka group id
- `SCRAPPER_URL` - url модуля scrapper для http запросов(`http://localhost:8080` - по умолчанию)

### Переменные окружения scrapper:
- `KAFKA_ENABLED` - используется для включения kafka в модуле(`true`/`false`), `true` - по умолчанию
- `UPDATES_TOPIC` - название топика с сообщениями об обновлении ссылок
- `TOPIC_PARTITIONS` - количество партиций топика
- `REPLICAS` - количество реплик топика
- `BOOTSTRAP_SERVERS` - адрес bootstrap сервера
- `BOT_URL` - url модуля bot для http запросов(`http://localhost:8090` - по умолчанию)
- `DB_URL` - url для подключения к бд(`jdbc:postgresql://localhost:5432/scrapper` - по умолчанию)
- `DB_PASSWORD` - пароль для подключения к бд(`postgres` - по умолчанию)
- `DB_USERNAME` - имя пользователя для подключения к бд(`postgres` - по умолчанию)
