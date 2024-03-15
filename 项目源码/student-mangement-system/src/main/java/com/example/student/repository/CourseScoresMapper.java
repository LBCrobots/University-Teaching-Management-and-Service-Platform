package com.example.student.repository;

import com.example.student.domain.Scores;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface CourseScoresMapper {

    @Select("SELECT course_id from s_student_score where student_id = #{studentId}")
    List<HashMap<String, Object>> findCourseIdByStudentId(Long studentId);

    @Update("UPDATE s_student_score set gradeclass_id = #{gradeClassId} where student_id = #{studentId}")
    void addGradeClassId(Long studentId, Long gradeClassId);

}
