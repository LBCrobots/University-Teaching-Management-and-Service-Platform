import { defineStore } from 'pinia'

export const useUserNoStore = defineStore({
    id: 'userNoStore',
        state: () => ({
        teachno: '',
        studentId: 0,
        gradeClassId: 0,
    }),
    actions: {
        setTeachno(teachno: string) {
            this.teachno = teachno
        },
        setStudentId(studentId: number) {
            this.studentId = studentId
        },
        setGradeClassId(gradeClassId: number) {
            this.gradeClassId = gradeClassId
        }
    }
})