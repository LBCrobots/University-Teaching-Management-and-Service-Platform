import { defineStore } from 'pinia'

export const useUserNoStore = defineStore({
    id: 'userNoStore',
        state: () => ({
        teachno: '',
        teachcourse: [],
        studentId: 0,
        gradeClassId: 0,
    }),
    actions: {
        setTeachno(teachno: string) {
            this.teachno = teachno
        },
        setTeachcourse(teachcourse: any[]) {
            this.teachcourse = teachcourse
        },
        setStudentId(studentId: number) {
            this.studentId = studentId
        },
        setGradeClassId(gradeClassId: number) {
            this.gradeClassId = gradeClassId
        }
    }
})