import {defineStore} from 'pinia'
export const useSettingStore = defineStore({
    id:'settingState',
    state: ()=>({
        /**
         * isCollapse: menu 是否折叠
         * showTag:    tagsView 是否展示 (默认展示)
         */
        isCollapse:true,
        showTag:true,
    }),
    getters: {},
    actions:{
        // 切换 Collapse
        setCollapse(value: boolean){
            this.isCollapse = value
        }
    }
})
