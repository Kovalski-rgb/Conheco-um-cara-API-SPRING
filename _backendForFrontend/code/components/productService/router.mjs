import {
  createProduct,
  createService,
  deleteTheProduct,
  deleteTheService,
  listEveryProduct,
  listEveryService,
  getProduct,
  getService
} from "./service.mjs";

/**
 * @openapi
 * /product:
 *  post:
 *    summary: "Creates a new Product"
 *
 *    tags:
 *       - "Product"
 *
 *    operationId: createNewProduct
 *    x-eov-operation-handler: productService/router
 *
 *    requestBody:
 *      description: Login information
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            $ref: '#/components/schemas/product'
 *
 *    responses:
 *      '200':
 *        description: "Created a new product"
 *      '403':
 *        description: "Some error ocurred during the product creating"
 *
 *    security:
 *      - JWT: ['USER']
 */
export async function createNewProduct(req, res, _) {
  const success = await createProduct(req);
  return success ? res.sendStatus(200) : res.sendStatus(403);
}

/**
 * @openapi
 * /service:
 *  post:
 *    summary: "Creates a new Service"
 *
 *    tags:
 *       - "Service"
 *
 *    operationId: createNewService
 *    x-eov-operation-handler: productService/router
 *
 *    requestBody:
 *      description: Login information
 *      required: true
 *      content:
 *        application/json:
 *          schema:
 *            $ref: '#/components/schemas/service'
 *
 *    responses:
 *      '200':
 *        description: "Created a new service"
 *      '403':
 *        description: "Some error ocurred during the service creating"
 *
 *    security:
 *      - JWT: ['USER']
 */
export async function createNewService(req, res, _) {
  const success = await createService(req);
  return success ? res.sendStatus(200) : res.sendStatus(403);
}

/**
 * @openapi
 * /product/{id}:
 *  delete:
 *    summary: "Request to delete a product"
 *
 *    tags:
 *       - "Product"
 *
 *    operationId: deleteProduct
 *    x-eov-operation-handler: productService/router
 *
 *    parameters:
 *       - $ref: "#/components/parameters/Id"
 *
 *    responses:
 *      '200':
 *        description: "Deleted the product"
 *      '403':
 *        description: "User does not have permission to delete this prodct"
 *      '404':
 *        description: "Product not found"
 *
 *    security:
 *      - JWT: ['USER']
 */
export async function deleteProduct(req, res, _) {
  const success = await deleteTheProduct(req);
  return success ? res.sendStatus(200) : res.sendStatus(401);
}

/**
 * @openapi
 * /service/{id}:
 *  delete:
 *    summary: "Request to delete a service"
 *
 *    tags:
 *       - "Service"
 *
 *    operationId: deleteService
 *    x-eov-operation-handler: productService/router
 *
 *    parameters:
 *       - $ref: "#/components/parameters/Id"
 *
 *    responses:
 *      '200':
 *        description: "Deleted the service"
 *      '403':
 *        description: "User does not have permission to delete this service"
 *      '404':
 *        description: "Service not found"
 *
 *    security:
 *      - JWT: ['USER']
 */
export async function deleteService(req, res, _) {
  const success = await deleteTheService(req);
  return success ? res.sendStatus(200) : res.sendStatus(401);
}

/**
 * @openapi
 * /product:
 *  get:
 *    summary: "List all products"
 *
 *    tags:
 *       - "Product"
 *
 *    operationId: listAllProducts
 *    x-eov-operation-handler: productService/router
 *
 *    responses:
 *      '200':
 *        description: "Listed all products successfully"
 *      '500':
 *        description: "Internal server error"
 *
 *    security:
 *      - {}
 */
export async function listAllProducts(req, res, _) {
  const products = await listEveryProduct();
  return res.json(products);
}

/**
 * @openapi
 * /service:
 *  get:
 *    summary: "List all service"
 *
 *    tags:
 *       - "Service"
 *
 *    operationId: listAllServices
 *    x-eov-operation-handler: productService/router
 *
 *    responses:
 *      '200':
 *        description: "Listed all services successfully"
 *      '500':
 *        description: "Internal server error"
 *
 *    security:
 *      - {}
 */
export async function listAllServices(req, res, _) {
  const services = await listEveryService();
  return res.json(services);
}

/**
 * @openapi
 * /products/{id}:
 *   get:
 *     summary: "Find product data by id"
 *
 *     tags:
 *       - "Product"
 *
 *     parameters:
 *       - $ref: "#/components/parameters/Id"
 *
 *     operationId: get_product
 *     x-eov-operation-handler: productService/router
 *
 *     responses:
 *       '200':
 *         description: "Returns the product data"
 *       '404':
 *         description: "Product not found"
 *     security:
 *       - {}
 *       - JWT: ['USER']
 */
export async function get_product(req, res, _) {
  if (!req.user) {
    return res.send("Guest user");
  }
  const product = await getProduct(req);
  return product ? res.json(product) : res.sendStatus(404);
}

/**
 * @openapi
 * /services/{id}:
 *   get:
 *     summary: "Find service data by id"
 *
 *     tags:
 *       - "Service"
 *
 *     parameters:
 *       - $ref: "#/components/parameters/Id"
 *
 *     operationId: get_service
 *     x-eov-operation-handler: productService/router
 *
 *     responses:
 *       '200':
 *         description: "Returns the service data"
 *       '404':
 *         description: "Service not found"
 *     security:
 *       - {}
 *       - JWT: ['USER']
 */
export async function get_service(req, res, _) {
  if (!req.user) {
    return res.send("Guest user");
  }
  const service = await getService(req);
  return service ? res.json(service) : res.sendStatus(404);
}

