package com.example.student.service.impl;

import com.example.student.domain.Course;
import com.example.student.domain.Student;
import com.example.student.domain.SysUser;
import com.example.student.domain.Teacher;
import com.example.student.repository.RegisterMapper;
import com.example.student.repository.UpdateMapper;
import com.example.student.service.IRegisterService;
import com.example.utils.Md5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements IRegisterService {

    private final RegisterMapper registerMapper;

    private final UpdateMapper updateMapper;
    @Override
    public SysUser getByUsername(String username) {
        return registerMapper.getByUsername(username);
    }

    @Override
    public void addRegister(SysUser sysUser) {
        // 如果密码未提供，则设置默认密码
        if (sysUser.getPassword() == null || sysUser.getPassword().isEmpty()) {
            sysUser.setPassword(Md5Util.MD5("123456"));
        }
        // 在保存到数据库之前对密码进行哈希处理
        sysUser.setPassword(Md5Util.MD5(sysUser.getPassword()));
        sysUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        if (sysUser.getSysRole().getId() == 2L){
            registerMapper.addRegister(sysUser);
            Teacher teacher = new Teacher();
            teacher.setCreateBy(1L);
            teacher.setCreateTime(sysUser.getCreateTime());
            teacher.setUpdateTime(sysUser.getUpdateTime());
            teacher.setRemarks(sysUser.getRemarks());
            teacher.setUpdateBy(sysUser.getUpdateBy());
            teacher.setName(sysUser.getRealname());
            teacher.setSex(sysUser.getSex());
            teacher.setUid(sysUser.getId());
            teacher.setTeachno("t00" + teacher.getUid());
            /*teacher.setCourse(updateMapper.getDefaultCourse());*/
            updateMapper.addTeacher(teacher);
        }
        else if (sysUser.getSysRole().getId() == 3L){
            registerMapper.addRegister(sysUser);
            Student student = new Student();
            student.setCreateBy(1L);
            student.setCreateTime(sysUser.getCreateTime());
            student.setUpdateBy(sysUser.getUpdateBy());
            student.setUpdateTime(sysUser.getUpdateTime());
            student.setRemarks(sysUser.getRemarks());
            student.setName(sysUser.getRealname());
            student.setSex(sysUser.getSex());
            student.setUid(sysUser.getId());
            updateMapper.addStudent(student);
        }
    }

    @Override
    public void UpdateRoleId(Long uid) {
        Long roleId = registerMapper.findRoleId(uid);
        updateMapper.insertStudentUID(roleId);
    }
}
