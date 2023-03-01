import { createCommunity, deleteTheCommunity, listEveryComunity,listEveryComunityFromUser, enterCommunity } from "./service.mjs";

/**
 * @openapi
 * /community/create:
 *  post:
 *    summary: "Creates a new Community"
 *    
 *    tags:
 *       - "Community"
 * 
 *    operationId: createNewCommunity
 *    x-eov-operation-handler: community/router
 * 
 *    requestBody:
 *      description: Login information
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            $ref: '#/components/schemas/community'
 *
 *    responses:
 *      '200':
 *        description: "Created a new community"
 *      '403':
 *        description: "Some error ocurred during the community creating"
 * 
 *    security:
 *      - JWT: ['USER']
 */
export async function createNewCommunity(req, res, _) {
    //const success = await createCommunity(req.user.id, req.body);
    const success = await createCommunity(req.user.id, req);
    return success ? res.sendStatus(200) : res.sendStatus(403);
}

/**
 * @openapi
 * /community/delete/{id}:
 *  delete:
 *    summary: "Request to delete a community"
 *    
 *    tags:
 *       - "Community"
 * 
 *    operationId: deleteCommunity
 *    x-eov-operation-handler: community/router
 * 
 *    parameters:
 *       - $ref: "#/components/parameters/Id"
 *
 *    responses:
 *      '200':
 *        description: "Deleted the community"
 *      '403':
 *        description: "User does not have moderator permission inside the community"
 *      '404':
 *        description: "Community not found"
 * 
 *    security:
 *      - JWT: ['USER']
 */
export async function deleteCommunity(req, res, _) {
    const success = await deleteTheCommunity(req);
    return success ? res.sendStatus(200) : res.sendStatus(401);
}

/**
 * @openapi
 * /community/all:
 *  get:
 *    summary: "List all communities"
 *    
 *    tags:
 *       - "Community"
 * 
 *    operationId: listAllComunities
 *    x-eov-operation-handler: community/router
 *
 *    responses:
 *      '200':
 *        description: "Created a new community"
 *      '401':
 *        description: "Users without authentication cannot create or join communities"
 * 
 *    security:
 *      - {}
 */
export async function listAllComunities(req, res, _){
    const communities = await listEveryComunity(); 
    return res.json(communities);
}

/**
 * @openapi
 * /community/me:
 *  get:
 *    summary: "List all communities that the logged user belongs"
 *    
 *    tags:
 *       - "Community"
 * 
 *    operationId: listAllUserComunities
 *    x-eov-operation-handler: community/router
 *
 *    responses:
 *      '200':
 *        description: "Created a new community"
 *      '401':
 *        description: "Users without authentication cannot create or join communities"
 * 
 *    security:
 *      - JWT: ['USER']
 */
export async function listAllUserComunities(req, res, _){
    const communities = await listEveryComunityFromUser(req); 
    return res.json(communities);
}

/**
 * @openapi
 * /community/join:
 *  post:
 *    summary: "Request to enter a community"
 *    
 *    tags:
 *       - "Community"
 * 
 *    operationId: enterOnCommunity
 *    x-eov-operation-handler: community/router
 * 
 *    requestBody:
 *      description: Community information
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            $ref: '#/components/schemas/communityParams'
 *
 *    responses:
 *      '200':
 *        description: "Created a new community"
 *      '403':
 *        description: "Some error ocurred during the community creating"
 * 
 *    security:
 *      - JWT: ['USER']
 */
export async function enterOnCommunity(req, res, _) {
    const success = await enterCommunity(req);
    return success ? res.sendStatus(200) : res.sendStatus(403);
}