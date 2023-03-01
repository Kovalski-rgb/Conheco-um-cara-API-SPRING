import { newAxios } from "../../lib/network.mjs";

export async function createProduct(req) 
  {
    const axios = newAxios();
    const response = await axios.post(
      "http://localhost:3005/api/product",
      {
        userId: req.user.id,
        name: req.body.name,
        description: req.body.description,
        price: req.body.price,
        productTypeId: req.body.productTypeId
      },
      {
        headers: { Authorization: req.header("Authorization") }
      }
    );
    return response.data;
  }

  export async function createService(req) 
  {
    const axios = newAxios();
    const response = await axios.post(
      "http://localhost:3005/api/service",
      {
        userId: req.user.id,
        name: req.body.name,
        description: req.body.description,
        price: req.body.price,
        serviceTypeId: req.body.serviceTypeId
      },
      {
        headers: { Authorization: req.header("Authorization") }
      }
    );
    return response.data;
  }

  export async function deleteTheProduct(req) {
    const axios = newAxios();
    const response = await axios.delete(
      `http://localhost:3005/api/product/${req.params.id}`,
      {
        headers: { Authorization: req.header("Authorization") }
      },
      {
        userId: req.user.id,
        body: req.body
      }

    );
    return response.data;
  }

  export async function deleteTheService(req) {
    const axios = newAxios();
    const response = await axios.delete(
      `http://localhost:3005/api/service/${req.params.id}`,
      {
        headers: { Authorization: req.header("Authorization") }
      },
      {
        userId: req.user.id,
        body: req.body
      }

    );
    return response.data;
  }  

  export async function listEveryProduct() {
    const axios = newAxios();
    const response = await axios.get("http://localhost:3005/api/product");
    return response.data;
  }
 
  export async function listEveryService() {
    const axios = newAxios();
    const response = await axios.get("http://localhost:3005/api/service");
    return response.data;
  }  

  export async function getProduct(req) {
    const axios = newAxios();
    const response = await axios.get(`http://localhost:3005/api/products/${parseInt(req.params.id)}`,
    {
        headers: { Authorization: req.header("Authorization") }
    }    
    );
    return response.data;
  }    

  export async function getService(req) {
    const axios = newAxios();
    const response = await axios.get(`http://localhost:3005/api/services/${parseInt(req.params.id)}`,
    {
        headers: { Authorization: req.header("Authorization") }
    }    
    );
    return response.data;
  }    

 