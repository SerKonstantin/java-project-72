# Website analyzer

[![Actions Status](https://github.com/SerKonstantin/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/SerKonstantin/java-project-72/actions)
[![Actions Status](https://github.com/SerKonstantin/java-project-72/actions/workflows/build.yml/badge.svg)](https://github.com/SerKonstantin/java-project-72/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/aa2bce53e386128eb3aa/maintainability)](https://codeclimate.com/github/SerKonstantin/java-project-72/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/aa2bce53e386128eb3aa/test_coverage)](https://codeclimate.com/github/SerKonstantin/java-project-72/test_coverage)

Website analyzer is a small Javalin project that allows you to check the accessibility of a specified website, verify the presence of the title and description, and store the information about these checks in the database.
Created for educational purposes.

Deployed on render.com: [https://site-analyzer-l13r.onrender.com](https://site-analyzer-l13r.onrender.com)

Caution: free version of web service on render.com works really slowly, so response time might take up to sixty seconds!
Potential expire date:  April 15, 2024

## Technologies used
- Javalin
- JTE, Bootstrap
- Lombok
- PostgreSQL, H2, JDBC
- JUnit, MockWebServer
- Docker

## To run locally
```shell
make install
```

then

```shell
make run-dist
```
