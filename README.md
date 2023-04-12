# Conheco-um-cara-API-SPRING
Conheco um cara API, 75% implementado em spring
<p align=center><a href="https://youtu.be/idk8NYyknmE">Apresentação da API</a></p>

## Dependencies to run:
 - [Maven](https://maven.apache.org/install.html)

## How to run?

 - First things first, clone the project:
 ```
 git clone https://github.com/Kovalski-rgb/Conheco-um-cara-API-SPRING.git
 cd Conheco-um-cara-API-SPRING
 ```

1 - Create three new databases on `mysql` under the adddress `localhost:3306`, with the following names:
```
authserver-spring
communityserver-spring
productAndServiceserver-spring
```

2 - Set the username on the settings file for each server, settings are located under
`src/main/resources/application.properties`
set the following lines with your user data
```
spring.datasource.username=[username]
spring.datasource.password=[password]
```

3 - If you are on linux, you can simply run the linux-starter.sh
```./linux-starter.sh```

3.1 - On windows, try running (THIS WAS NOT TESTED TO RUN ON WINDOWS):
```
mvn -f "communityServer/pom.xml" spring-boot:run
mvn -f "authServer/pom.xml" spring-boot:run
mvn -f "productAndServiceServer/pom.xml" spring-boot:run
```

4 - Your server should be up and running, you should be able to access swagger-ui with the following links:
- Auth Server: http://localhost:8080/authserver/api/swagger-ui/index.html


- Community Server: http://localhost:8081/communityserver/api/swagger-ui/index.html


- Product and Service Server: http://localhost:8082/productAndServiceserver/api/swagger-ui/index.html



## TODO list
 - [x] Finish functional requisites
    - [x] AuthServer
    - [x] P&S_Server
    - [x] CommunityServer
       - [x] Moderators
       - [x] Posts
          - [x] Products
          - [x] Services
 - [ ] Finish non-functional requisites
    - [ ] AuthServer
       - [x] Generate a default admin user (when users in database = 0, first is admin)
       - [ ] Admins toggle other admin roles
    - [ ] P&S_Server
       - [ ] Admin route to get all products & services (with pagination)
       - [ ] Admin routes on CRUD, by id
    - [ ] CommunityServer
       - [x] Route to get all info about one community, admin use only
       - [x] Route to get all members from community
       - [x] Change GET /community/all behaviour to NOT retrieve community codes
       - [x] POST /community/create automatically adds the creator to the created community, sets him as moderator
       - [x] PUT and DELETE /community/{id} should only be used by community moderators and admins
       - [x] Admins should only have power do delete/edit communities, not interfere inside them (deleting posts, changing mods, etc)
       - [x] Admins and moderators should be able to kick users inside communities
       - [ ] When deleting a community, delete all its posts too
 - [x] Unitary tests
    - [x] AuthServer
       - [x] GET /users/me
       - [x] PUT /users/me
       - [x] DELETE /users/me
       - [x] POST /users/login
       - [x] POST /users/create
       - [x] GET /users/{id} (admin perm)
       - [x] DELETE /users/{id} (admin perm)
    - [x] P&S_Server
       - [x] PUT /service/{id}
       - [x] DELETE /service/{id}
       - [x] POST /service/register
       - [x] GET /service/me
       - [x] GET /service/me/{page}
       - [x] PUT /product/{id}
       - [x] DELETE /product/{id}
       - [x] POST /product/register
       - [x] GET /product/me/{page}
       - [x] GET /product/all/{page}
    - [x] CommunityServer
       - [x] PUT /community/{id}
       - [x] DELETE /community/{id}
       - [x] POST /community/{communityId}/kick/{userId}
       - [x] POST /community/moderators/toggle
       - [x] POST /community/leave
       - [x] POST /community/join
       - [x] POST /community/create
       - [x] GET /community/{communityId}
       - [x] GET /community/{communityId}/moderators/
       - [x] GET /community/{communityId}/members/
       - [x] GET /community/me
       - [x] GET /community/all
       - [x] PUT /post/{postId}/community/{communityId}
       - [x] DELETE /post/{postId}/community/{communityId}
       - [x] POST /post/create
       - [x] GET /post/me
       - [x] GET /post/community/{id}
       - [x] GET /post/community/{id}/me
 - [x] Documentation
    - [x] AuthServer routes
    - [x] P&S_Server routes
    - [x] CommunityServer routes
 - [ ] Stress tests
    - [ ] Entering communities
    - [ ] Creating posts
    - [ ] Leaving Communities
    - [ ] Requesting communities
 - [ ] Finish BFF
