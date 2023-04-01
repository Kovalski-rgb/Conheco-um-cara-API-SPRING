# Conheco-um-cara-API-SPRING
Conheco um cara API, 75% implementado em spring

### TODO list
 - [ ] Finish functional requisites
    - [x] AuthServer
    - [x] P&S_Server
    - [ ] CommunityServer
       - [ ] Moderators
       - [ ] Posts
          - [ ] Products
          - [ ] Services
 - [ ] Finish non-functional requisites
    - [ ] AuthServer
       - [ ] Generate a default admin user
       - [ ] Admins toggle other admin roles
    - [ ] P&S_Server
       - [ ] Admin route to get all products (with pagination)
       - [ ] Admin routes on CRUD, by id
    - [ ] CommunityServer
       - [ ] Change GET /community behaviour to NOT retrieve community codes
       - [ ] POST /community/create automatically adds the creator to the created community, sets him as moderator
       - [ ] PUT and DELETE /community/{id} should only be used by community moderators and admins
 - [ ] Unitary tests
    - [ ] AuthServer
       - [ ] GET /users/me
       - [ ] PUT /users/me
       - [ ] DELETE /users/me
       - [ ] POST /users/testUser
       - [ ] POST /users/login
       - [ ] POST /users/create
       - [ ] GET /users/{id} (admin perm)
       - [ ] DELETE /users/{id} (admin perm)
    - [ ] P&S_Server
       - [ ] PUT /service
       - [ ] DELETE /service
       - [ ] POST /service/register
       - [ ] GET /service/me
       - [ ] PUT /product
       - [ ] DELETE /product
       - [ ] POST /product/register
       - [ ] GET /product/me
    - [ ] CommunityServer (WIP)
 - [ ] Stress tests
    - [ ] Entering communities
    - [ ] Creating posts
    - [ ] Leaving Communities
    - [ ] Requesting communities
 - [ ] Finish BFF
