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
    - [ ] CommunityServer
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
       - [ ] PUT /post/{postId}/community/{communityId}
       - [x] DELETE /post/{postId}/community/{communityId}
       - [x] POST /post/create
       - [x] GET /post/me
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
