package com.example.student.domain;

import com.example.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：系统角色实体类*/
@Data
@Entity
@Table(name = "sys_role")
@org.hibernate.annotations.Table(appliesTo = "sys_role",comment="系统角色信息表")
public class SysRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 角色名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 角色编号
     */
    @Column(name = "code")
    private String code;

}
