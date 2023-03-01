import { registerUser, login, loadById, updateUser, deleteCurrentUser, deleteUser } from "./service.mjs";

/**
 * @openapi
 * /users/login:
 *   post:
 *     summary: "Logs in the user. If the log in was successfull return a token."
 *
 *     tags:
 *       - "Authentication"
 *
 *     operationId: user_login
 *     x-eov-operation-handler: user/router
 *
 *     requestBody:
 *       description: Login information
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: "#/components/schemas/UsernamePassword"
 *
 *     responses:
 *       '200':
 *         description: "User logged in"
 *       '400':
 *         description: "Invalid data provided"
 *       '401':
 *         description: "Login failed"
 */
export async function user_login(req, res, _) {
  const user = await login(req.body);
  return user ? res.json(user) : res.sendStatus(401);
}

/**
 * @openapi
 * /users/{id}:
 *   get:
 *     summary: "Retrieves user information"
 *
 *     tags:
 *       - "Profile"
 *
 *     parameters:
 *       - $ref: "#/components/parameters/Id"
 *
 *     operationId: get_user
 *     x-eov-operation-handler: user/router
 *
 *     responses:
 *       '200':
 *         description: "Returns the user"
 *       '404':
 *         description: "User not found"
 *     security:
 *       - {}
 *       - JWT: ['USER']
 */
export async function get_user(req, res, _) {  
  const user = await loadById(req);
  return user ? res.json(user) : res.sendStatus(404);  
}

/**
 * @openapi
 * /users/me:
 *  get:
 *    summary: "Gets currently logged user, user need to be logged in"
 *  
 *    tags:
 *      - "Profile"
 *  
 *    operationId: get_current_user
 *    x-eov-operation-handler: user/router
 *  
 *    responses:
 *      '200':
 *        description: "New user registered successfully"
 *      '400':
 *        description: "Invalid data provided"
 *      '401':
 *        description: "Registration failed"
 * 
 *    security:
 *      - {}
 *      - JWT: ['USER']
 */
export async function get_current_user(req, res, _){

  if(!req.user){
    return res.send("Guest user");
  }
  const user = await loadById(req);
  return user ? res.json(user) : res.sendStatus(404); 
}

/**
 * @openapi
 * /users:
 *   post:
 *     summary: "Creates a new user."
 *
 *     tags:
 *       - "Profile"
 *
 *     operationId: user_register
 *     x-eov-operation-handler: user/router
 *
 *     requestBody:
 *       description: New user information
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: "#/components/schemas/NewUserInfo"
 *
 *     responses:
 *       '200':
 *         description: "New user registered successfully"
 *       '400':
 *         description: "Invalid data provided"
 *       '401':
 *         description: "Registration failed"
 */
export async function user_register(req, res, _) {
  const user = await registerUser(req.body);
  return user ? res.sendStatus(200) : res.sendStatus(401);
}

/**
 * @openapi
 * /info:
 * 
 *  get:
 *    summary: Retrieves Developer Information
 * 
 *    tags:
 *      - "Misc"
 * 
 *    operationId: dev_info
 *    x-eov-operation-handler: user/router
 * 
 *    responses:
 *      '200':
 *        description: "Developer info retrieved successfully"
 */
export async function dev_info(req, res, _){
  return res.json([
    {
      "name": "André Luiz Kovalski",
      "github": "https://github.com/Kovalski-rgb"
    },
    {
      "name": "Carlos Mareo Suzuki",
      "github": "https://github.com/carlosuzuki"
    },
    {
      "name": "Fernando Andrey Borman",
      "github": "https://github.com/fborman"
    }
  ]);
}

/**
 * @openapi
 * /users/me:
 *  put:
 *    summary: "Updates user data, user need to be logged in"
 *
 *    tags:
 *      - "Profile"
 *
 *    operationId: user_update
 *    x-eov-operation-handler: user/router
 *
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            $ref: "#/components/schemas/UserInfo" 
 *
 *    responses:
 *      200:
 *        description: Update sucesfully
 *      401:
 *        description: Unauthorized put request
 *      500:
 *        description: Some errors happened.
 *
 *    security:
 *      - JWT: ['USER']
 */
//todo: Fix!!!
export async function user_update(req, res, _) {
  if(!req.user){ return res.sendStatus(401); }
  const userData = await updateUser(req);
  return userData ? res.sendStatus(200) : res.sendStatus(500);
}

/**
 * @openapi
 * /users/me:
 *  delete:
 *    summary: "Delete own user data, user need to be logged in"
 *
 *    tags:
 *      - "Profile"
 * 
 *    operationId: delete_current_user
 *    x-eov-operation-handler: user/router
 *
 *    responses:
 *      200:
 *        description: data is deleted
 *      401:
 *        description: Unauthorized put request
 *      500:
 *        description: Some errors happened.
 *
 *    security:
 *      - JWT: ['USER']
 */
export async function delete_current_user(req, res, _) {
  if(!req.user){ return res.sendStatus(401); }
  const userDataDelete = await deleteCurrentUser(req);
  return userDataDelete ? res.sendStatus(200) : res.sendStatus(500);
}

/**
 * @openapi
 * /users/{id}:
 *  delete:
 *    summary: "Delete user by id. Admin permission needed"
 *
 *    tags:
 *      - "Profile"
 * 
 *    parameters:
 *      - $ref: "#/components/parameters/Id"
 * 
 *    operationId: delete_user
 *    x-eov-operation-handler: user/router
 *
 *    responses:
 *      200:
 *        description: data is delete
 *      401:
 *        description: Unauthorized put request
 *      500:
 *        description: Some errors happened.
 *
 *    security:
 *      - JWT: ['ADMIN']
 */
export async function delete_user(req, res, _){
  if(!req.user){ res.sendStatus(401); }
  const userDataDelete = await deleteUser(req);
  return userDataDelete ? res.sendStatus(200) : res.sendStatus(500);
}


