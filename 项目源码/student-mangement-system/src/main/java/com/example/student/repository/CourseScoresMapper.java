package com.example.student.repository;

import com.example.student.domain.Scores;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CourseScoresMapper {

    @Select("SELECT course_id from s_student_score where student_id = #{studentId}")
    List<HashMap<String, Object>> findCourseIdByStudentId(Long studentId);


}
