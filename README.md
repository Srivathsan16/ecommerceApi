# Handelsbank E-commerce API
## Overview
The Handelsbank E-commerce API provides backend services for an e-commerce platform, enabling features such as product checkout with dynamic pricing adjustments based on discounts. This project is implemented using Spring Boot and utilizes JPA for database interactions.

### Prerequisites
- Java JDK 17 or later
- Maven 3.6 or later
- Docker
- An SQL database (MySQL)

### Run the docker compose for mysql 8.0 server

Fire up the Docker daemon and run the following command to start the MySQL server:
```
Open Terminal and go to the root of the project 
cd local-setup
Then run : docker-compose up
```
This will start the mysql server on port 3306

### Build the application:

```
mvn clean install package
```
The above command also runs the test cases.

## Build and Run the application

The application can be directly run as a Spring Boot application from the IDE or by running the following command from the terminal:
 and also the application can be run as a docker container

### Build Docker Image
To build the Docker image, run the application please make sure to give the IP address in the application.properties file.

- Go to the root of the project where the Dockerfile is and run the following command:
```
docker build -t ecommerce-api .
```
On Successful build you can run the project using the following command:
```
docker run -p 8080:8080 ecommerce-api
```
Once the application is successfully up please use the below sample curl command to test the application:
```
curl -X POST http://localhost:8080/checkout \
     -H "Content-Type: application/json" \
     -d '[ "001", "001", "001", "002", "002" ]'

```

## API Endpoints

### Checkout Process

- **Endpoint**: `POST /checkout`
- **Description**: This endpoint handles the checkout process for purchasing items. It calculates the total cost based on the product IDs provided.
- **Request Headers**:
    - `Accept: application/json`
    - `Content-Type: application/json`

- **Request Body**:
    - **Description**: Array of product IDs representing the items to be purchased.
    - **Format**:
      ```json
      [
        "001", "002", "001", "004", "003"
      ]
      ```

- **Response**:
    - **Headers**:
        - `Content-Type: application/json`
    - **Body**:
        - **Description**: Returns the total price of the items after applying any applicable discounts.
        - **Format**:
          ```json
          {
            "price": 360
          }
          ```

### Usage Example

To use this endpoint to calculate the total price of selected items:

```bash
curl -X POST http://localhost:8080/checkout \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '[ "001", "002", "001", "004", "003" ]'
```

## Error Handling and Exceptions

### Overview

The Handelsbank E-commerce API is designed with robust error handling to ensure clear and informative feedback is provided to the client applications. This section describes the custom exceptions and error handling implemented in the API.

### Custom Exceptions

The API defines several custom exceptions to handle specific scenarios gracefully:

- **NoItemsToCheckoutException**: This exception is thrown when a checkout request is made with no items.
- **EntityNotFoundException**: Used for database entities that cannot be found.
- **IllegalArgumentException**: Thrown when an invalid argument is passed to a method.
- **General Exceptions**: Any other exceptions not specifically handled fall into this category.

### Exception Handling

The API uses `@RestControllerAdvice` to handle exceptions globally. This ensures that all exceptions are caught and handled gracefully, returning a consistent error response structure.

#### Exception Handlers

- **EntityNotFoundException Handler**: Returns a `404 Not Found` status with a descriptive error message.
- **IllegalArgumentException Handler**: Returns a `400 Bad Request` status with details about the bad request.
- **NoItemsToCheckoutException Handler**: Specifically handles the `NoItemsToCheckoutException` by returning a `400 Bad Request` with a message indicating that no items were provided for checkout.
- **General Exception Handler**: Catches all other exceptions and returns a `500 Internal Server Error`, indicating an unexpected condition was encountered.

### Error Response Format

All error responses follow a consistent format to facilitate easy parsing by client applications:

```json
{
  "message": "Detailed error message",
  "error": "Short error or status description",
  "status": "HTTP status code"
}
```
## Rate Limiting

### Overview

The Handelsbank E-commerce API incorporates a simple rate limiting mechanism to control the rate of traffic from individual clients. This is crucial for protecting the API from excessive use and ensuring equitable resource usage among clients.

### Implementation Details

Rate limiting is implemented using Aspect-Oriented Programming (AOP) to provide a clean and reusable approach. It is managed by the `RateLimitingAspect` class, which intercepts API calls and enforces rate limits.

### Annotations

A custom annotation `@RateLimit` is used to apply rate limiting to any method. This annotation allows the configuration of rate limits directly in the service layer.

#### Attributes

- `requests`: Maximum number of allowed requests.
- `duration`: Time period (in milliseconds) in which the number of requests is calculated.

### How It Works

1. **Aspect Configuration**: The `RateLimitingAspect` class uses the `@Before` advice to intercept methods annotated with `@RateLimit` before their execution.
2. **Request Tracking**: Each client's IP address is used as a key to track the number of requests made within the specified duration.
3. **Rate Limit Enforcement**: If the number of requests exceeds the allowed limit within the duration, a `RuntimeException` with the message "Rate limit exceeded" is thrown, blocking further execution of the method.

## Further Improvements
    - Security can be added
    - More test cases can be added
    - OpenAPI documentation can be added
    - Caching if required can be added
    - bicep code can be added for the infrastructure
        