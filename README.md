# Conheco-um-cara-API-SPRING
Conheco um cara API, 75% implementado em spring

### TODO list
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
       - [ ] Generate a default admin user
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
 - [ ] Unitary tests
    - [ ] AuthServer
       - [x] GET /users/me
       - [ ] PUT /users/me
       - [ ] DELETE /users/me
       - [x] POST /users/login
       - [x] POST /users/create
       - [x] GET /users/{id} (admin perm)
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
    - [ ] CommunityServer
       - [ ] PUT /community/{id}
       - [ ] DELETE /community/{id}
       - [ ] POST /community/{communityId}/kick/{userId}
       - [ ] POST /community/moderators/toggle
       - [ ] POST /community/leave
       - [ ] POST /community/join
       - [ ] POST /community/create
       - [ ] GET /community/create
       - [ ] GET /community/{communityId}/moderators/
       - [ ] GET /community/{communityId}/members/
       - [ ] GET /community/me
       - [ ] GET /community/all
       - [ ] PUT /post/{postId}/community/{communityId}
       - [ ] DELETE /post/{postId}/community/{communityId}
       - [ ] POST /post/create
       - [ ] GET /post/me
       - [ ] GET /post/community/{id}
       - [ ] GET /post/community/{id}/me
 - [ ] Documentation
    - [ ] AuthServer routes
    - [ ] P&S_Server routes
    - [ ] CommunityServer routes
 - [ ] Stress tests
    - [ ] Entering communities
    - [ ] Creating posts
    - [ ] Leaving Communities
    - [ ] Requesting communities
 - [ ] Finish BFF
