import { newAxios } from "../../lib/network.mjs";
import axios from "axios";



export async function login({ email, password }) {
  const axios = newAxios();
  const response = await axios.post("http://localhost:3001/api/users/login", {
    email: email,
    password: password,
  });
  return response.data;
}

export async function loadById(req) {
  const response = await axios.get(`http://localhost:3001/api/users/${parseInt(req.user.id)}`,{
    headers: { Authorization: req.header("Authorization") }
  });
  return response.data;
}

export async function updateUser(req){
  const headers = {
    'Authorization': req.header('Authorization'),
    'Content-Type:': req.header('Content-Type')
  }
  const axios = newAxios();
  const response = await axios.put("http://localhost:3001/api/users/me", {
    header: headers,
    data: { ... req.body }
  });
  return response.data;
}

export async function registerUser({ name, email, password }) {
  const axios = newAxios();
  const response = await axios.post("http://localhost:3001/api/users", {
    name: name,
    email: email,
    password: password,
  });
  return response.data;
}

export async function deleteCurrentUser(req) {
  const headers = {
    'Authorization': req.header('Authorization')
  }
  const response = await axios.delete(`http://localhost:3001/api/users/${req.user.id}`,{headers});
  return response.data;
}

export async function deleteUser(req) {
  const headers = {
    'Authorization': req.header('Authorization')
  }
  const response = await axios.delete(`http://localhost:3001/api/users/${req.params.id}`,{headers});
  return response.data;
}




