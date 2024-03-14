package com.example.student.service.dto;

import com.example.annotation.EnableQuery;
import lombok.Data;

/**功能描述：系统用户查询条件参数*/
@Data
public class UserQueryCriteria {

    /**
     * 根据用户名、真实姓名、邮箱模糊查询
     */
    @EnableQuery(blurry = "username,realname,email")
    private String searchValue;


    /**
     * 根据真实姓名模糊查询
     */
    @EnableQuery
    private String sex;


    /**
     * 状态
     */
    @EnableQuery()
    private String status;
}
