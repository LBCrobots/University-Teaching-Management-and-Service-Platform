import axios from 'axios'
import { useUserStore } from '../store/modules/user'
const token = useUserStore().token

const service  = axios.create({
    // baseURL: import.meta.env.VITE_APP_BASE_API,
    baseURL: '/api',
    timeout: 3000000,
    withCredentials: true,
})

// Axios 请求前置拦截器 | 加入token
service.interceptors.request.use( config => {
    if (!config.headers) config.headers = {};
    token? config.headers.token= token : ''
    return config
}, error => {
    console.log("[Interceptors Error]" + error);
})

export default service
