# KIET Student Academic Management REST API

Simple Spring Boot project for the mock coding assessment. It uses an in-memory `ArrayList`; no database is required.

## Requirements
- Java 17 or later
- Maven (or open `pom.xml` in IntelliJ IDEA)

## Run in IntelliJ IDEA
1. Open this project folder or its `pom.xml`.
2. Allow IntelliJ to load Maven dependencies.
3. Run `src/main/java/com/kiet/student/StudentApplication.java`.
4. The server runs on `http://localhost:8080`.

## Endpoints
- `GET /students` — get all students
- `GET /students/101` — get a student by ID
- `POST /students` — add a student (JSON body)
- `PUT /students/101` — update a student (JSON body)
- `DELETE /students/105` — delete a student
- `GET /students/search?branch=CSE&year=2` — search by branch and year
- `GET /students/topper?cgpa=9.0` — find students with CGPA >= 9.0

For POST and PUT in Postman, select Body -> raw -> JSON.
