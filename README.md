# Restaurant-voting
Graduation project from [Topjava](https://javaops.ru/view/topjava)

##  Table of content
- Technical requirement
- Swagger
- cURLs

## Technical requirement:
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
  -  If it is before 11:00 we assume that he changed his mind.
  -  If it is after 11:00 then it is too late, vote can't be changed
- Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it (**better - link to Swagger**).

###### P.S.: Make sure everything works with latest version that is on github :)
###### P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.


## [Swagger](http://localhost:8080/swagger-ui/index.html)
#### Credentials for authorization:
- User: 
  - email: user@gmail.com
  - password: password 
- Admin:
  - email: admin@javaops.ru
  - password: admin


## cURLs for tests
##### **Admin API: administration of dishes (get All)**

- Get all menu (or by date):

```console
curl -X GET --location "http://localhost:8080/admin/restaurants/dishes" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

##### **Admin API: administration of users**

  - Get all:

```console
curl -X GET --location "http://localhost:8080/admin/users" -H "Accept: application/json"  --basic --user admin@javaops.ru:admin
```

  - Get one:

```console
curl -X GET --location "http://localhost:8080/admin/users/1" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Delete:

```console
curl -X DELETE --location "http://localhost:8080/admin/users/1" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```
 
 - Create:

```console
curl -X POST --location "http://localhost:8080/admin/users" -H "Content-Type: application/json" -d "{\"name\": \"string\",\"password\": \"string\",\"email\": \"string@gmail.com\",\"enabled\": true,\"roles\": [\"USER\"]}" --basic --user admin@javaops.ru:admin
```

  - Disable:

```console
curl -X PATCH --location "http://localhost:8080/admin/users/1?enabled=false" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Update:

```console
curl -X PUT --location "http://localhost:8080/admin/users/1" -H "Content-Type: application/json" -d "{\"name\": \"string\",\"password\": \"string\",\"email\": \"string@gmail.com\",\"enabled\": true,\"roles\": [\"USER\"]}" --basic --user admin@javaops.ru:admin
```

##### **Admin API: administration of restaurants**

  -Update (put):

```console
curl -X PUT --location "http://localhost:8080/admin/restaurants/1" -H "Content-Type: application/json" -d "{\"name\": \"New name\"}"--basic --user admin@javaops.ru:admin
```

  - Get all:

```console
curl -X GET --location "http://localhost:8080/admin/restaurants" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Get one:

```console
curl -X GET --location "http://localhost:8080/admin/restaurants/1" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Delete:

```console
curl -X DELETE --location "http://localhost:8080/admin/restaurants/1" --basic --user admin@javaops.ru:admin
```

  - Update (change name):

```console
curl -X PATCH --location "http://localhost:8080/admin/restaurants/1?name=name" -H "Content-Type: application/json" --basic --user admin@javaops.ru:admin
```

  - Create:

```console
curl -X POST --location "http://localhost:8080/admin/restaurants" -H "Content-Type: application/json" -d "{\"name\": \"string\"}" --basic --user admin@javaops.ru:admin
```

##### **Admin API: administration of dishes**

  - Update (put):

```console
curl -X PUT --location "http://localhost:8080/admin/restaurants/1/dishes/1" -H "Content-Type: application/json" -d "{\"name\": \"string\",\"price\": 100,\"localDate\": \"2022-09-15\"}"--basic --user admin@javaops.ru:admin
```

  - Get menu of restaurant by date:

```console
curl -X GET --location "http://localhost:8080/admin/restaurants/1/dishes?localDate=2000-05-11" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Get one by id:

```console
curl -X GET --location "http://localhost:8080/admin/restaurants/1/dishes/1" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Delete:

```console
curl -X DELETE --location "http://localhost:8080/admin/restaurants/1/dishes/1" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Update:

```console
curl -X PATCH --location "http://localhost:8080/admin/restaurants/1/dishes/1?name=string" --basic --user admin@javaops.ru:admin
```

  - Create:

```console
curl -X POST --location "http://localhost:8080/admin/restaurants/1/dishes" -H "Content-Type: application/json" -d "{\"name\": \"string\",\"price\": 100,\"localDate\": \"2022-09-10\"}"--basic --user admin@javaops.ru:admin
```

##### **Admin API: administration of votes**

  - Get rate of restaurant by date:

```console
curl -X GET --location "http://localhost:8080/admin/restaurants/1/votes/rate?localDate=2000-05-11" --basic --user admin@javaops.ru:admin
```

  - Get all for restaurant by date:

```console
curl -X GET --location "http://localhost:8080/admin/restaurants/1/votes?localDate=2000-05-11" --basic --user admin@javaops.ru:admin
```

##### **User API: profile**

  - Get vote:

```console
curl -X GET --location "http://localhost:8080/profile/my-vote" -H "Accept: application/json" --basic --user admin@javaops.ru:admin
```

  - Get user self:

```console
curl -X GET --location "http://localhost:8080/profile" -H "Accept: application/json" --basic --user user@gmail.com:password
```

  - Delete user self:

```console
curl -X DELETE --location "http://localhost:8080/profile" --basic --user user@gmail.com:password
```

  - Update user self:

```console
curl -X PUT --location "http://localhost:8080/profile" -H "Content-Type: application/json" -d "{\"name\": \"string\",\"password\": \"string\",\"email\": \"string@gmail.com\",\"enabled\": true,\"roles\": [\"USER\"]}" --basic --user user@gmail.com:password
```

##### **User API: restaurant**

  - Get all:

```console
curl -X GET --location "http://localhost:8080/restaurants" -H "Accept: application/json" --basic --user user@gmail.com:password
```

  - Get one:

```console
curl -X GET --location "http://localhost:8080/restaurants/1" -H "Accept: application/json" --basic --user user@gmail.com:password
```

##### **User API: dishes**

  - Get lunch menu of restaurant:

```console
curl -X GET --location "http://localhost:8080/restaurants/1/dishes" -H "Accept: application/json" --basic --user user@gmail.com:password
```

##### **User API: votes**

  - Make vote:

```console
curl -X POST --location "http://localhost:8080/restaurants/1/votes" --basic --user admin@javaops.ru:admin
```

  - Try to change vote:

```console
curl -X PATCH --location "http://localhost:8080/restaurants/1/votes" --basic --user admin@javaops.ru:admin
```

  - Get rate of restaurant:

```console
curl -X GET --location "http://localhost:8080/restaurants/1/votes/rate" --basic --user user@gmail.com:password
```
