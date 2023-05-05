## Book Dealer Service

1. I didn't create unit test due to simple logic.
2. Created functional and integration tests.

### docker files
1. Dockerfile-amd64 - for ARM
2. Dockerfile - for x86

### set up service
1. mvn clean package
2. docker build -f Dockerfile-amd64 -t bookdealer-service:1.0 .
3. docker run -d -p 8080:8080 bookdealer-service:1.0
4. swagger http://localhost:8080/swagger-ui.html

### postman process flow

### Author

#### 1. sing-up
   
POST http://localhost:8080/api/v1/auth/signup

body:

{
"username" : "Kafka",
"email" : "kafka@gmail.com",
"password" : "qwertyuiop",
"role" : [
"admin"
]
}

Success response:

{
"message": "User registered successfully!"
}

#### 2. sing-in

POST http://localhost:8080/api/v1/auth/signin

body:

{
"username" : "Kafka",
"password" : "qwertyuiop"
}

Success response:

{
"id": 1,
"username": "Kafka",
"email": "kafka@gmail.com",
"roles": [
"ROLE_ADMIN"
]
}

#### 3. create author

POST http://localhost:8080/api/v1/authors

body:

{
"firstName": "Franz",
"lastName": "Kafka",
"dateOfBirth": "1883-03-23",
"books": [
{
"title": "Spring Boot Master",
"isbn": "978-3-16-148410-0",
"publicationDate": "2000-03-05",
"price": 234.76
},
{
"title": "Hibernate Master",
"isbn": "978-3-16-148410-0f",
"publicationDate": "2010-03-05",
"price": 134.76
}
]
}

Success response:

{
"id": 1,
"firstName": "Franz",
"lastName": "Kafka",
"dateOfBirth": "1883-03-23",
"books": [
{
"id": 1,
"title": "Spring Boot Master",
"isbn": "978-3-16-148410-0",
"publicationDate": "2000-03-05",
"price": 234.76
},
{
"id": 2,
"title": "Hibernate Master",
"isbn": "978-3-16-148410-0",
"publicationDate": "2010-03-05",
"price": 134.76
}
]
}

#### 4. update author

PUT http://localhost:8080/api/v1/authors/1

body:

{
"firstName": "Franz",
"lastName": "Kafka",
"dateOfBirth": "1883-06-03", # UPDATE
"books": [
{
"title": "Spring Boot Master # UPDATE",
"isbn": "978-3-16-148410-0",
"publicationDate": "2000-03-05",
"price": 234.76
},
{
"title": "Hibernate Master",
"isbn": "978-3-16-148410-0f",
"publicationDate": "2010-03-05",
"price": 134.76
}
]
}

Success response:

204 No content

#### 5. get all author

GET http://localhost:8080/api/v1/authors

Success response:

[
{
"id": 1,
"firstName": "Franz",
"lastName": "Kafka",
"dateOfBirth": "1883-06-03",
"books": [
{
"id": 1,
"title": "Spring Boot Master # UPDATE",
"isbn": "978-3-16-148410-0",
"publicationDate": "2000-03-05",
"price": 234.76
},
{
"id": 2,
"title": "Hibernate Master",
"isbn": "978-3-16-148410-0f",
"publicationDate": "2010-03-05",
"price": 134.76
}
]
}
]

#### 6. get by id author

GET http://localhost:8080/api/v1/authors/1

Success response:

{
"id": 1,
"firstName": "Franz",
"lastName": "Kafka",
"dateOfBirth": "1883-06-03",
"books": [
{
"id": 1,
"title": "Spring Boot Master # UPDATE",
"isbn": "978-3-16-148410-0",
"publicationDate": "2000-03-05",
"price": 234.76
},
{
"id": 2,
"title": "Hibernate Master",
"isbn": "978-3-16-148410-0f",
"publicationDate": "2010-03-05",
"price": 134.76
}
]
}


#### 7. get books by author

GET http://localhost:8080/api/v1/authors/1/books

Success response:

[
{
"id": 1,
"title": "Spring Boot Master # UPDATE",
"isbn": "978-3-16-148410-0",
"publicationDate": "2000-03-05",
"price": 234.76
},
{
"id": 2,
"title": "Hibernate Master",
"isbn": "978-3-16-148410-0f",
"publicationDate": "2010-03-05",
"price": 134.76
}
]

#### 8. delete author and books cascading

DELETE http://localhost:8080/api/v1/authors/1

Success response:

204 No content

#### 9. filter, sorting, pagination by firstname

GET http://localhost:8080/api/v1/authors/sorted/firstname?firstName=Spring&page=0&size=1

### Book

#### create book

POST http://localhost:8080/api/v1/books

body:

{
"title" : "Kafka Master",
"isbn" : "978-3-16-148410-0",
"publicationDate" : "2022-03-05",
"price" : 400.76,
"authorId" : 1
}

Success response:

{
"id" : 3,
"title" : "Kafka Master",
"isbn" : "978-3-16-148410-0",
"publicationDate" : "2022-03-05",
"price" : 400.76,
"authorId" : 1
}

#### get all book

GET http://localhost:8080/api/v1/books

Success response:

[
{
"id": 1,
"title": "Spring Boot Master # UPDATE",
"isbn": "978-3-16-148410-0",
"publicationDate": "2000-03-05",
"price": 234.76
},
{
"id": 2,
"title": "Hibernate Master",
"isbn": "978-3-16-148410-0f",
"publicationDate": "2010-03-05",
"price": 134.76
},
{
"id" : 3,
"title" : "Kafka Master",
"isbn" : "978-3-16-148410-0",
"publicationDate" : "2022-03-05",
"price" : 400.76,
"authorId" : 1
}
]

#### get book by id

GET http://localhost:8080/api/v1/books/3

Success response:

{
"id" : 3,
"title" : "Kafka Master",
"isbn" : "978-3-16-148410-0",
"publicationDate" : "2022-03-05",
"price" : 400.76,
"authorId" : 1
}

#### update book

PUT http://localhost:8080/api/v1/books/3

body:

{
"title" : "Kafka Master",
"isbn" : "978-3-16-148410-0",
"publicationDate" : "2022-03-05",
"price" : 500.76, # UPDATE
"authorId" : 1
}

Success response:

204 No content

#### delete book

DELETE http://localhost:8080/api/v1/books/1

Success response:

204 No content

#### filter, sorting, pagination by isbn

GET http://localhost:8080/api/v1/books/sorted/isbn?isbn=889-987&page=0&size=1

