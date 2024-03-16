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

export function addSubjectsApi(studentId:number,courseId:number,gradeClassId:string) {
    return request({
        url: 'scores/addCourseSelect',
        method: 'post',
        data: {
            "student": {
                "id":studentId,
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

export function getScoresByTeacherUidApi(uid:number, data:object) {
    return request({
        url: `scores/getStudentsByTeacherCourses`,
        method: 'get',
        params: {
            uid: uid,
            ...data
        }
    })
}

export function getCourseByStudentUIdApi(uid:number, data:object) {
    return request({
        url: `scores/getStudentScoresList`,
        method: 'get',
        params: {
            studentUId: uid,
            ...data
        }
    })
}