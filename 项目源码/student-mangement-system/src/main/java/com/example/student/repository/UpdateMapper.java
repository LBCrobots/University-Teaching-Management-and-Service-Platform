package com.example.student.repository;

import com.example.student.domain.Course;
import com.example.student.domain.Student;
import com.example.student.domain.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UpdateMapper {

    @Insert("INSERT INTO s_student(uid) VALUES(#{uid})")
    int insertStudentUID(Long uid);

    @Select("SELECT * from s_course where id = 0")
    Course getDefaultCourse();

    /*新增教师信息*/
    @Insert("INSERT into s_teacher(create_by, create_time, update_by, update_time, name, sex, teach_no, course_id, uid, remarks) " +
            "values (#{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{name}, #{sex}, #{teachno}, 0, #{uid}, #{remarks})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addTeacher(Teacher teacher);

    /*新增学生信息*/
    @Insert("INSERT into s_student(create_by, create_time, update_by, update_time, name, sex, uid, remarks, stuno, grade_class_id) " +
            "values (#{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{name}, #{sex}, #{uid}, #{remarks}, #{stuno}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void  addStudent(Student student);

    /*根据uid查询教师信息*/
    @Select("SELECT * from s_teacher where uid = #{uid}")
    Teacher getByTeacherUid(Long uid);

    /*根据uid更新教师信息*/
    void updateByTeacherUid(Teacher teacher);

    @Select("SELECT * from s_student where uid = #{uid}")
    Student getByStudentUid(Long uid);

    void updateByStudentUid(Student student);

    @Delete("DELETE from s_teacher where uid = #{uid}")
    void deleteTeacher(Long uid);

    @Delete("DELETE from s_student where uid = #{uid}")
    void deleteStudent(Long uid);

    /*根据用户id获取其role类型*/
    @Select("SELECT role_id from sys_user where id = #{id}")
    Long getRole(Long id);
}
