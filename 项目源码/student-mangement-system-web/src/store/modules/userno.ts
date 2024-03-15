import { defineStore } from 'pinia'

export const useUserNoStore = defineStore({
    id: 'userNoStore',
        state: () => ({
        teachno: '',
        teachcourse: [],
        studentId: '',
        gradeClassId: '',
    }),
    actions: {
        setTeachno(teachno: string) {
            this.teachno = teachno
        },
        setTeachcourse(teachcourse: any[]) {
            this.teachcourse = teachcourse
        },
        setStudentId(studentId: string) {
            this.studentId = studentId
        },
        setGradeClassId(gradeClassId: string) {
            this.gradeClassId = gradeClassId
        }
    }
})