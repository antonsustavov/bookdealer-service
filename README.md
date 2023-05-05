## Book Dealer Service

1. I didn't create unit test due to simple logic.
2. Created functional and integration tests.

#### docker files
1. Dockerfile-amd64 - for ARM
2. Dockerfile - for x86

#### set up service
1. mvn clean package
2. docker build -f Dockerfile-amd64 -t bookdealer-service:1.0 .
3. docker run -d -p 8080:8080 bookdealer-service:1.0
4. swagger http://localhost:8080/swagger-ui.html