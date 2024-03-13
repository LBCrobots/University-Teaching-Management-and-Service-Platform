package com.example.student.repository;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TokenMapper {
    @Insert("insert into token values (#{token}) ")
    public void save(String token);

    @Select("select * from token")
    public String get();

    @Delete("delete from token")
    public void delete();
}
