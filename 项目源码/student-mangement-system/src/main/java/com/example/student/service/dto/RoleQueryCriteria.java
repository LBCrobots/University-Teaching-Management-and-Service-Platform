package com.example.student.service.dto;

import com.example.annotation.EnableQuery;
import lombok.Data;

/**功能描述：系统角色查询条件*/
@Data
public class RoleQueryCriteria {
    /**
     * 根据角色名称、角色编号模糊查询
     */
    @EnableQuery(blurry = "name,code")
    private String searchValue;
}
