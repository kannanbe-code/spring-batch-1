# spring-batch-1
Spring Batch project source with full working versions, Oracle DB integration, REST API chunked submission, SQL file reader, retry, logging, and Dockerfile.

# Spring Batch Oracle REST Job

This Spring Batch application:
- Loads data from Oracle using a multi-join SQL query
- Submits records in chunks to a REST API
- Uses retry logic and logs everything
- Supports Docker and test data

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


