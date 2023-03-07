import { isProd } from "./env.mjs";
import { error, info } from "./logger.mjs";

export class ServerError extends Error {
    statusCode;
    errors;
    static HANDLERS = [logHandler, sendHandler];

    constructor(message = "Internal Server Error", statusCode = 500) {
        super(message);
        this.name = `ServerError_${statusCode}`;
        this.statusCode = statusCode;
        this.errors = [];
    }

    add(error) {
        this.errors.append(error);
        return this;
    }

    static throwIf(condition, message, builder = badRequest) {
        if (condition) throw builder(message);
        return ServerError;
    }

    static throwIfNot(condition, message, builder = badRequest) {
        if (!condition) throw builder(message);
        return ServerError;
    }

}

export const badRequest = (message = "Bad Request") =>
    new ServerError(message, 400);

export const unauthorized = (message = "Unauthorized") =>
    new ServerError(message, 401);

export const forbidden = (message = "Forbidden") =>
    new ServerError(message, 403);

export const notFound = (message = "Not found") => {
    const id = parseInt(message.id || message);
    if (isNaN(id)) return new ServerError(message, 404);
    return new ServerError(`Not Found: ${id}`, 404);
}

export const unsuportedMediaType = (type) =>
    new ServerError(`Unsuported Media Type ${type}`, 415);

export function toJSON(err) {
    const type = `${err.statusCode || err.errorCode || err.status || err.code || "500?"}`;
    let statusCode = parseInt(type);
    if (isNaN(statusCode) || statusCode < 400 || statusCode > 599) statusCode = 500;
    const message = err.message;
    const stack = err.stack;

    const json = {
        type,
        statusCode,
        message,
        stack
    }
    if (err.errors) json.errors = err.errors;
    if (err.cause) json.errors = [err.cause];
    return json;
}

export function sendError(res, err) {
    const error = toJSON(err);
    console.log('isProd()');
    console.log(error.stack);
    if (isProd()) {
        delete error.stack;
        delete error.errors;
        if (error.statusCode === 500) error.message = "Internal Server Error"
    }
    res.status(error.statusCode).json(error);
}

function sendHandler(err, req, res, next) {
    sendError(res, err);
}

export function logHandler(err, req, res, next) {
    const jsonError = toJSON(err);
    const description = `${req.method} ${req.url} [ERROR: ${err.statusCode} ]`
    const log = {
        description,
        err,
        env: process.env.NODE_ENV,
        agent: req.headers['user-agent'],
        method: req.method,
        path: req.path,
        params: Object.keys(req.params).length === 0 ? undefined : req.params,
        query: Object.keys(req.query).length === 0 ? undefined : req.query,
        body: Object.keys(req.body).length === 0 ? undefined : req.body,
        user: req.user
    }
    jsonError.statusCode === 500 ? error(log) : info(log);
    if (next) next(err);
}