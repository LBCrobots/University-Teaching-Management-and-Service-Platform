package com.example.student.service.dto;

import com.example.annotation.EnableQuery;
import lombok.Data;

/**功能描述：成绩查询功能*/
@Data
public class ScoresQueryCriteria {

    /**
     * 根据班级ID查询
     */
    @EnableQuery(joinName = "gradeClass",propName = "id",type = EnableQuery.Type.EQUAL)
    private Long gradeClassId;

    /**
     * 手机号
     * 左关联查询，left Join ， joinName为关联实体名称 , propName为关联实体 字段
     * type = EnableQuery.Type.INNER_LIKE 表示中模糊查询
     */
    @EnableQuery(joinName = "student",propName = "stuno",type = EnableQuery.Type.INNER_LIKE)
    private String stuno;

    /**
     * 根据学生姓名模糊查询
     */
    @EnableQuery(joinName = "student",propName = "name",type = EnableQuery.Type.INNER_LIKE)
    private String name;

    /**
     * 根据课程名称模糊查询
     */
    @EnableQuery(joinName = "course",propName = "coursename",type = EnableQuery.Type.INNER_LIKE)
    private String coursename;

    /**
     * 根据课程ID查询
     */
    @EnableQuery(joinName = "course",propName = "id",type = EnableQuery.Type.EQUAL)
    private Long courseId;

}
