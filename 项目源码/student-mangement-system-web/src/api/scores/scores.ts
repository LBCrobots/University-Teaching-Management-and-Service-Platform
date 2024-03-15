import request from '../request'
export function getScoresListApi(data:object) {
    return request({
        url: 'scores',
        method: 'get',
        params: data
    })
}
// 批量选课
export function registerScoresApi(gradeClassId:number,courseId:number) {
    return request({
        url: 'scores',
        method: 'post',
        data: {
            gradeClassId: gradeClassId,
            courseId: courseId
        }
    })
}

// 更新成绩
export function editScoresApi(id:number,score: number) {
    return request({
        url: 'scores',
        method: 'put',
        data: {
            id:id,
            score:score
        }
    })
}
// 根据ID删除成绩信息
export function deleteScoresApi(id:number) {
    return request({
        url: `scores/${id}`,
        method: 'delete'
    })
}

//TODO 根据学生uid获取课程信息
export function getCourseByStudentUidApi(uid:string) {
    return request({
        url: `course/uid/${uid}`,
        method: 'get'
    })
}

//TODO 添加课程
export function addSubjectsApi(studentId:number,courseId:number,gradeClassId:number) {
    return request({
        url: 'scores/addCourseSelect',
        method: 'post',
        data: {
            student: studentId,
            course: courseId,
            gradeClass: gradeClassId
        }
    })
}

//TODO 根据教师uid获取课程信息
export function getCourseByTeacherUidApi(uid:string) {
    return request({
        url: `course/uid/${uid}`,
        method: 'get'
    })
}
