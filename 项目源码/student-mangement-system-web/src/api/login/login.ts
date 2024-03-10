import request from '../request'
export function loginApi(data:object) {
    return request({
        url: 'login',
        method: 'post',
        data
    })
}

// 退出系统
export function loginOutApi() {
    return request({
        url: 'loginOut'
    })
}

// 注册Api
export function registerApi(data:object) {
    return request({
        url: 'register',
        method: 'post',
        data
    })
}
