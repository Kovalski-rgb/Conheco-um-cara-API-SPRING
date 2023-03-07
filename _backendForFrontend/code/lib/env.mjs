export function isDev(){
    return process.env.NODE_ENV === 'dev'
}

export function isCI(){
    return process.env.NODE_ENV === 'ci'
}

export function isProd(){
    return process.env.NODE_ENV === 'prod'
}

export function isStaging(){
    return !isProd();
}

export function getServerPort(){
    return process.env.PORT;
}