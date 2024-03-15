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

export function addSubjectsApi(studentId:string,courseId:number,gradeClassId:string) {
    return request({
        url: 'scores/addCourseSelect',
        method: 'post',
        data: {
            "student": {
                "id":parseInt(studentId),
            },
            "course": {
                "id":courseId,
            },
            "gradeClass": {
                "id":parseInt(gradeClassId),
            }
        }
    })
}

//TODO 根据教师uid获取教师所教课程，通过所教课程查找对应课程的成绩
export function getScoresByTeacherUidApi(uid:number, data:object) {
    return request({
        url: `scores/getScoresByTeacherUid`,
        method: 'get',
        data: uid,
        params: data
    })
}

//TODO 根据学生id获取课程信息
export function getCourseByStudentIdApi(id:number, data:object) {
    return request({
        url: `scores/getStudentScoresList`,
        method: 'get',
        data: id,
        params: data
    })
}