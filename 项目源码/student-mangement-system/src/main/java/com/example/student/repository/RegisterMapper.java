package com.example.student.repository;

import com.example.student.domain.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

@Mapper
public interface RegisterMapper {
    @Select("select * from sys_user where username = #{username}")
    SysUser getByUsername(String username);


    @Insert("INSERT into sys_user(password, username, realname, sex, status, email, role_id, create_time, update_time, create_by, update_by) " +
            "values (#{password}, #{username}, #{realname}, #{sex}, #{status}, #{email}, #{sysRole.id}, #{createTime}, #{updateTime}, 1, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addRegister(SysUser sysUser);

    @Select("SELECT role_id from sys_user where id = #{id}")
    Long findRoleId(Long id);
}
