# API Gateway

API Gateway for HIMFG microservices.

## Prerequisites

*   Java 17
*   Maven 3.8+
*   Docker

## Building the project

To build the project, run the following command from the root directory:

```bash
mvn clean install
```

## Running the project

### Locally

You can run the project locally with the following command:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8081`.

### With Docker

First, build the Docker image:

```bash
docker build -t himfg/api-gateway .
```

Then, run the Docker container:

```bash
docker run -p 8081:8081 himfg/api-gateway
```
