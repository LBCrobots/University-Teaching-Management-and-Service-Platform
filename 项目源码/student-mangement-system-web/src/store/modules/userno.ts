import { defineStore } from 'pinia'

export const useUserNoStore = defineStore({
    id: 'userNoStore',
        state: () => ({
        teachno: '',
        teachcourse: []
    }),
    actions: {
        setTeachno(teachno: string) {
            this.teachno = teachno
        },
        setTeachcourse(teachcourse: any[]) {
            this.teachcourse = teachcourse
        }
    }
})