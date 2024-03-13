import {defineStore} from 'pinia'
export const useUserStore = defineStore({
    id:'userStore',
    state: ()=>{
        /**
         * token:    登录token
         * userInfo: 登录用户信息
         * roles:    角色
         */
        return {
            token: '',
            userInfo:{} as UserInfo,
            roles:[]
        }
    },
    getters: {},
    actions:{
        // 设置登录token
        setToken(token:string){
            this.token = token;
        },
        // 设置登录用户信息
        setUserInfo(userInfo:any){
            this.userInfo = userInfo
        },
        // 更新基础用户信息
        setBasicUserInfo({realname, sex, userIcon}:BasicUserInfo){
            this.userInfo.realname = realname
            this.userInfo.sex = sex
            this.userInfo.userIcon = userIcon
        }
    },
    persist: true
})

interface BasicUserInfo {
    realname: string
    sex: string
    userIcon: string
}

interface UserInfo {
    createTime: string
    email: string
    realname: string
    sex: string
    userIcon: string
    role: {}
}
