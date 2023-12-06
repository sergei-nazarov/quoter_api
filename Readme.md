# Quoter

The service is deployed on a rented server.  http://sergeiprojects.ru:8080

Docker compose file is also included (./docker/compose.yaml)

## Api

All api definitions available by url http://sergeiprojects.ru:8080/swagger-ui/index.html

Postman collection https://api.postman.com/collections/15518308-9e6c25f6-87c7-44b3-9290-f3ee95f2ee26?access_key=PMAT-01HGYZ9YZZW8ZPKFQT3B0NTVYX

Authorization header is used for authorization ("BASIC login:password", login:password in base 64 of course).

### User api

1. Create new user. POST localhost:8080/user/register
2. Get current user info. GET localhost:8080/user
3. Get quotes of the current user. GET localhost:8080/user/quotes
4. Get votes of the current user. GET localhost:8080/user/votes

### Quote api

1. Create quote. POST localhost:8080/quote
2. Get quote by id. GET localhost:8080/quote/{id}
3. Delete quote by id. DELETE localhost:8080/quote/{id}
4. Update quote by id. PUT localhost:8080/quote/{id}
5. Get user info by username. GET localhost:8080/users/{username}
6. Get quotes of a user by username. GET localhost:8080/users/{username}/quotes
7. Get random quote. GET localhost:8080/quote/random
8. Get last quotes. GET localhost:8080/quotes
9. Get TOP RATING quotes. GET localhost:8080/quotes?sort=rating,DESC
10. Get WORSE quotes. GET localhost:8080/quotes?sort=rating,ASC
11. Rate quote. POST localhost:8080/quote/{id}/rate
12. Get data for the "time-rating" graph. GET localhost:8080/quote/{id}/graph






