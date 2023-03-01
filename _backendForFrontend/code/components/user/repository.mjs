import { json } from 'express';
import { badRequest, notFound, ServerError, unauthorized } from '../../lib/errors.mjs';


const USER_FIELDS = {
    id: true,
    email: true,
    name: true,
    roles: true,
    password: false
}


export async function filterCommunityByUserId(allCommunitiesInf, userId) {
    allCommunitiesInf.json.filter(function(list) {
        return list[users.value] === userId;
    });
    return "Not found any community with this user.";
}
