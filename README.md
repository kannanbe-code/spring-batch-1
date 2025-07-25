# spring-batch-1
Spring Batch project source with full working versions, Oracle DB integration, REST API chunked submission, SQL file reader, retry, logging, and Dockerfile.

# Spring Batch Oracle REST Job

This Spring Batch application:
- Executes a complex SQL with joins (from a `.sql` file)
- Splits results into chunks
- Submits each chunk as JSON to a REST API
- Uses retry mechanism
- Logs results
- Writes failures to a file
- Supports Oracle DB, Docker, and Postman collection

# Spring Batch Oracle to REST API Project

This project reads data from an Oracle database using complex SQL joins, chunks and sends records to a REST API in JSON format, with retry, error fallback, scheduler, and auditing.

---

## 🔧 Features

- Spring Batch 5.x (no deprecated APIs)
- Chunk-oriented processing
- SQL file-based item reader (complex joins)
- REST submission (with retry + fallback)
- Exception handling + logging
- Email or file fallback for failed items
- Step-level auditing with `StepExecutionListener`
- Spring Scheduler (cron-driven execution)
- Multi-environment support (dev, test, prod)
- Oracle DB with batch schema + test data
- JUnit test coverage with `application-test.yml`
- Dockerfile + Docker Compose support
- Lombok-based clean code

---

## 🔁 Profiles Setup

| Profile | DB         | Scheduler | Schema Init | REST Endpoint |
|---------|------------|-----------|-------------|----------------|
| dev     | Oracle     | Every 5m  | Enabled     | Dev API        |
| prod    | Oracle     | Hourly    | Disabled    | Prod API       |
| test    | H2 (in-mem)| Every min | Enabled     | Mock endpoint  |

---

## 🚀 How to Run

```bash
# Default profile (dev)
mvn spring-boot:run

# For test or prod
mvn spring-boot:run -Dspring.profiles.active=test

# Run JUnit tests with test profile
mvn test -Dspring.profiles.active=test

# Build image
docker build -t spring-batch-app .

# Run with docker-compose
docker-compose --profile dev up

📂 Configuration Files
All configuration files are inside src/main/resources/:

application.yml — base profile activator

application-dev.yml — dev environment

application-prod.yml — production environment

application-test.yml — test (JUnit + H2)

## 💻 Technologies
- Spring Boot 3.1.5
- Spring Batch
- Oracle DB (ojdbc8)
- REST API submission with `RestTemplate`
- RetryTemplate for fault tolerance
- Logging with SLF4J
- Postman collection included

## 🚀 How to Run

### 1. Setup Oracle DB

```sql
CREATE TABLE customers (id NUMBER PRIMARY KEY, name VARCHAR2(100));
CREATE TABLE products (id NUMBER PRIMARY KEY, name VARCHAR2(100));
CREATE TABLE orders (
  id NUMBER PRIMARY KEY,
  customer_id NUMBER,
  product_id NUMBER,
  quantity NUMBER,
  order_date DATE,
  status VARCHAR2(20),
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

2. Build & Run App

mvn clean package
java -jar target/spring-batch-oracle-rest-job-1.0.0.jar

Or run with Docker:

docker build -t spring-batch-oracle-job .
docker run -p 8080:8080 spring-batch-oracle-job

3. Test with Postman

Import postman/spring-batch-api.postman_collection.json into Postman and hit http://localhost:8081/api/process.

Use Postman to simulate receiving service:

# Mock listener for POST
npm install -g json-server
json-server --watch db.json --routes routes.json

Project Structure:

spring-batch-oracle-rest-job/
├── src/
│   ├── main/
│   │   ├── java/com/example/batch/
│   │   │   ├── BatchApplication.java
│   │   │   ├── config/BatchConfig.java
│   │   │   ├── listener/JobCompletionListener.java
│   │   │   ├── model/CustomerOrderProductDTO.java
│   │   │   ├── reader/SqlFileItemReader.java
│   │   │   └── writer/RestApiWriter.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── sql/complex_query.sql
│   ├── test/
│   │   └── java/com/example/batch/BatchApplicationTests.java
│
├── Dockerfile
├── README.md
├── pom.xml
├── postman/spring-batch-api.postman_collection.json
└── test-data/test-data.sql

Batch Details:

Reads using JdbcTemplate + SQL file
Submits chunked items to REST
Retries up to 3 times with 2s delay
All activities logged
Errors can be written to file or email (extendable)


---

## 🚀 How It Works

1. **Reader** loads the SQL file and executes a query with joins on Oracle
2. Results are mapped to `CustomerOrderProductDTO`
3. Writer uses `RestTemplate` to submit data to a REST API
4. On failure, retries 3 times (configurable)
5. Failed records are logged to a file or emailed
6. Job lifecycle is logged with listener

---

## 🧪 Run Locally

### Prerequisites

- Oracle XE (or use Docker)
- Java 17+
- Maven 3.x
- Postman (for REST tests)

### 1. Run Oracle DB

Install locally or run with Docker:
```bash
docker run -d -p 1521:1521 -e ORACLE_PASSWORD=batch_password gvenzl/oracle-xe

Execute test data:

docker cp oracle-init.sql <container-id>:/tmp/
docker exec -it <container-id> bash
sqlplus system/password@XE @/tmp/oracle-init.sql

Retry & Logging:

Configured in RetryTemplate
Max attempts: 3 (default), configurable via application.yml
All processing, errors, and job summaries logged to logs/

