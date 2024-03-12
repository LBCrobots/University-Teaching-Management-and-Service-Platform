import {defineStore} from 'pinia'
export const useUserNoStore = defineStore({
    id:'userNoStore',
    state: ()=>{
        return {
            token: '',
            userInfo:{},
            roles:[]
        }
    },
    getters: {},
    actions:{
        setToken(token:string){
            this.token = token;
        },
        setUserInfo(userInfo:any){
            this.userInfo = userInfo
        }
    },
    persist: true
})
