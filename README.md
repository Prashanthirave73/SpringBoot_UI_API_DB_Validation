# API + UI + Database Automation Framework

This project contains a local Spring Boot practice application and a TestNG automation framework that validates the API, UI, and MySQL database layers against the same records.

## Stack

- Java 17+
- Spring Boot local practice app
- MySQL
- Selenium WebDriver
- Rest Assured
- TestNG
- Maven
- JDBC repository pattern
- Jackson POJOs
- Extent Reports
- SLF4J + Log4j2
- DataProvider and parallel execution support

## Run

1. Start MySQL:

```bash
docker compose up -d
```

2. Start the practice application:

```bash
mvn spring-boot:run
```

3. In another terminal, run the automation suite:

```bash
mvn test
```

The UI is available at `http://localhost:8080`.

## Useful Configuration

Edit `src/test/resources/config.properties` or pass Maven properties:

```bash
mvn test -Dheadless=true -Dbase.url=http://localhost:8080
```

Extent report output:

```text
target/extent-report/AutomationReport.html
```

## End-to-End Coverage

`api.tests.UserLifecycleE2ETest` performs:

1. Create user through `POST /users`.
2. Verify the user through JDBC.
3. Login through Selenium UI.
4. Fetch user through `GET /users/{id}` and compare against database.
5. Partially update user address through `PATCH /users/{id}` and verify JDBC state.
6. Fully update user through `PUT /users/{id}`.
7. Verify database and UI show the updated state.
8. Create order through `POST /orders`.
9. Verify order through JDBC.
10. Verify order through Selenium UI.
11. Delete user through `DELETE /users/{id}`.
12. Verify database deletion.
13. Verify UI login failure.

If Docker is not installed, create a local MySQL schema named `automation_db` and update `src/test/resources/config.properties` plus `src/main/resources/application.properties` with your local credentials.
