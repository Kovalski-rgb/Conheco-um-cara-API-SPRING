import axios from 'axios';


export function forwarder(baseurl, req, res, auth=null) { //url==null
    return async(req, res, method= null, body=null) => {
        const headers = {
            'Authorization': auth || req.header('Authorization'),
            'Content-Type:': req.header('Content-Type')
        }
  
        if (!method) method = axios[req.method.toLowerCase()];
        //let response = await method(`${baseurl}/${req.url}`, {
        let response = await method(`${baseurl}/${req.url}`, {
            headers,
            data: body || { ... req.body }
        });
        res.status(response.status).send(res.data);
    }
  }

/*
function forwarder(baseurl, auth=null) { //url==null
    return async(req, res, method= null, body=null) => {
        const headers = {
            'Authorization': auth || req.header('Authorization'),
            'Content-Type:': req.header('Content-Type')
        }
  
        if (!method) method = axios[req.method.toLowerCase()];
        let response = await method(`${baseurl}/${req.url}`, {
            headers,
            data: body || { ... req.body }
        });
        res.status(response.status).send(res.data);
    }
  }*/