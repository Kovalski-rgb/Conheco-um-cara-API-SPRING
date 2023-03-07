import jwt from 'jwt-simple'

function dateToSec(date = new Date()) {
    return Math.floor(date.getTime() / 1000);
}

function secToDate(s){
    return new Date(s * 1000);
}

function hasAnyRole(user, roles = []){
    if(roles.size === 0)
        return true;
    if(!user || !user.roles) 
        return false;
    return user.roles.some(r=>roles.includes(r));
}

function expandClaims(token) {
    const coersed = {...token};
    if (token.sub) {
        coersed.subject = token.sub;
        delete coersed.sub;
    }
    if (token.iss) {
        coersed.issuer = token.iss;
        delete coersed.iss;
    }
    if (coersed.exp) {
        coersed.expiration = secToDate(token.exp);
        delete coersed.exp;
    }
    if (coersed.iat) {
        coersed.issuedAt = secToDate(token.iat);
        delete coersed.iat;
    }
    if (coersed.aud) {
        coersed.audience = token.aud;
        delete coersed.aud;
    }
    if (coersed.nbf) {
        coersed.notBefore = secToDate(token.nbf);
        delete coersed.nbf;
    }
    if (coersed.jti) {
        coersed.jwtId = coersed.jti;
        delete coersed.jti;
    }
    return coersed;
}

export function decode(bearerString, noVerify = false){
    if(!bearerString) 
        return {};
    return expandClaims(jwt.decode(bearerString.replace("Bearer ", ""), process.env.TOKEN_SECRET, noVerify));
}

export function JWT_SECURITY(req, scopes=[]){
    if(scopes.size === 0){
        scopes.push('USER');
    }

    const token = decode(req.header("Authorization"));

    if(!token.user || token.issuer !== process.env.ISSUER){
        throw {
            status: 401, 
            message: 'Unauthorized'
        };
    }
    
    if(!hasAnyRole(token.user, scopes)){
        throw {
            status: 401, 
            message: 'Unauthorized' +
            process.env.NODE_ENV !== 'Production' ? 
                `. Roles: [${token.user.roles}] Needed: [${scopes}]` :''
        };
    }

    req.token = token;
    req.user = token.user;
    return true;
}