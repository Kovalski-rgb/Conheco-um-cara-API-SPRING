import express from 'express';
import cookieParser from 'cookie-parser';
import logger from 'morgan';

import { dirname } from 'path';
import { fileURLToPath } from 'url';

import OpenApiValidator from 'express-openapi-validator';
import swaggerJSDoc from 'swagger-jsdoc';
import swaggerUi from 'swagger-ui-express'
import resolver from '../lib/esmresolver.mjs';
import { JWT_SECURITY } from '../lib/jwt.mjs';
import { isDev } from '../lib/env.mjs';

const __dirname = dirname(fileURLToPath(import.meta.url));
const app = express();

const BASE_PATH = `${__dirname}/../components`
const MORGAN_FMT = isDev() ? "dev" : ":remote-addr :method :url HTTP/:http-version :status :res[content-length] - :response-time ms :user-agent";



app.use(logger(MORGAN_FMT, {
  skip: (req, res) => {
    req.url.includes("echo") && (res.statusCode == 200 || res.status === 304)
  }
}));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());

const swaggerOptions = {
  swaggerDefinition: {
    openapi: "3.0.3",
    info: {
      title: "BFF Server",
      version: "0.1.0",
      description: "Backend For Frontend server."
    },
    servers: [{
      url: "http://localhost:3007/api",
      description: "BFF server"
    }]
  },
  apis: [
    `${BASE_PATH}/**/*.yaml`,
    `${BASE_PATH}/**/*.mjs`
  ]
};

const swaggerDocs = swaggerJSDoc(swaggerOptions);
delete swaggerDocs.channels;

app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocs));

app.use(OpenApiValidator.middleware({
  apiSpec: swaggerDocs,
  validateSecurity: {
    handlers: {
      JWT: JWT_SECURITY
    }
  },
  operationHandlers: {
    basePath: BASE_PATH,
    resolver
  }
}));

// serverErrors
app.use(express.static(`${__dirname}/../public`));

export default app;
