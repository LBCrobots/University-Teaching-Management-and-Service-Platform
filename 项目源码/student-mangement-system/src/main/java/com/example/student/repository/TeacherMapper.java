package com.example.student.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TeacherMapper {
    @Select("SELECT course_id from s_teacher where uid = #{teacherUid}")
    List<HashMap<String, Object>> findCourseIdByTeacherUid(Long teacherUid);
}
