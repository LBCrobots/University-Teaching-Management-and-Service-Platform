import { defineStore } from 'pinia'

export const useUserNoStore = defineStore({
    id: 'userNoStore',
        state: () => ({
        teachno: '',
        stuno: ''
    }),
    actions: {
        setTeachno(teachno) {
            this.teachno = teachno
        },
        setStuno(stuno) {
            this.stuno = stuno
        }
    }
})