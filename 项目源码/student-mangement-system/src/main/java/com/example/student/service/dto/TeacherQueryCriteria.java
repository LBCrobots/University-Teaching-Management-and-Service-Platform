package com.example.student.service.dto;

import com.example.annotation.EnableQuery;
import lombok.Data;

/**功能描述：教师信息查询条件*/
@Data
public class TeacherQueryCriteria {
    /**
     * 根据教师姓名、教师工号、手机号、qq模糊查询
     */
    @EnableQuery(blurry = "name,teachno,phone,qq")
    private String searchValue;
}
