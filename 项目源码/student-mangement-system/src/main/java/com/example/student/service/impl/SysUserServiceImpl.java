package com.example.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.student.domain.Student;
import com.example.student.domain.SysRole;
import com.example.student.domain.SysUser;
import com.example.student.domain.Teacher;
import com.example.student.repository.SysUserRepository;
import com.example.student.repository.UpdateMapper;
import com.example.student.service.ISysUserService;
import com.example.student.service.dto.UserQueryCriteria;
import com.example.student.vo.ModifyPwdModel;
import com.example.utils.Md5Util;
import com.example.utils.PageUtil;
import com.example.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;

/**功能描述：系统用户业务接口实现类*/
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserRepository sysUserRepository;

    private final UpdateMapper updateMapper;
    /**
     * 登录
     * @param sysUser
     * @return
     */
    @Override
    public SysUser login(SysUser sysUser) {
        SysUser dbSysUser = sysUserRepository.findByusername(sysUser.getUsername());
        return dbSysUser;
    }

    @Override
    public boolean getUser(String username){
        SysUser dbSysUser = sysUserRepository.findByusername(username);
        return dbSysUser.getUsername()!=null;
    }

    @Override
    public SysUser addRegister(Map<String, Object> userMap) {
        // 创建 SysUser 对象并设置相关属性
        SysUser sysUser = new SysUser();
        sysUser.setUsername((String) userMap.get("username"));
        sysUser.setRealname((String) userMap.get("realname"));
        sysUser.setEmail((String) userMap.get("email"));
        sysUser.setSex((String) userMap.get("sex"));
        sysUser.setUserIcon((String) userMap.get("userIcon"));
        sysUser.setSysRole((SysRole) userMap.get("role"));
        /*sysUser.setId(sysUserRepository.findMaxId());*/
        // 设置创建时间为当前时间
        sysUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        // 将用户保存到数据库
        SysUser dbSysUser = sysUserRepository.save(sysUser);

        return dbSysUser;
    }



    /**
     * 获取用户列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(UserQueryCriteria queryCriteria, Pageable pageable) {
        Page<SysUser> page = sysUserRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 新增用户信息
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(SysUser sysUser) {
        sysUser.setPassword(Md5Util.MD5(sysUser.getPassword()));
        sysUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        SysUser dbSysUser = sysUserRepository.save(sysUser);
        log.info("获得的role是：{}",sysUser.getSysRole().getId());
        Long roleId = updateMapper.getRole(sysUser.getId());
        if (roleId == 2L){

            Teacher teacher = new Teacher();
            teacher.setCreateBy(1L);
            teacher.setCreateTime(sysUser.getCreateTime());
            teacher.setUpdateBy(1L);
            teacher.setUpdateTime(sysUser.getUpdateTime());
            teacher.setRemarks(sysUser.getRemarks());
            teacher.setName(sysUser.getRealname());
            teacher.setSex(sysUser.getSex());
            teacher.setUid(sysUser.getId());
            teacher.setTeachno("t00" + teacher.getUid());
            updateMapper.addTeacher(teacher);
        }
        else if (roleId == 3L){

            Student student = new Student();
            student.setCreateBy(1L);
            student.setCreateTime(sysUser.getCreateTime());
            student.setUpdateBy(1L);
            student.setUpdateTime(sysUser.getUpdateTime());
            student.setName(sysUser.getRealname());
            student.setSex(sysUser.getSex());
            student.setUid(sysUser.getId());
            updateMapper.addStudent(student);
        }
        return dbSysUser.getId()!=null;
    }

    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    @Override
    public SysUser getById(Long id) {
        return sysUserRepository.findById(id).orElseGet(SysUser::new);
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(SysUser sysUser) {
        //获取对应id的内容
        SysUser dbSysUser = sysUserRepository.getReferenceById(sysUser.getId());
        //将sysUser内容复制到dbSysUser里
        BeanUtil.copyProperties(sysUser,dbSysUser, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        // 如果密码不为空，进行加密处理
        if (StringUtils.isNotBlank(sysUser.getPassword())) {
            String encryptedPassword = Md5Util.MD5(sysUser.getPassword());
            dbSysUser.setPassword(encryptedPassword);
        }
        //根据id找到teacher表或者student表的uid，实现更新
        Long roleId = updateMapper.getRole(sysUser.getId());
        Long uid = sysUser.getId();
        if (roleId == 2) {
            Teacher dbTeacher = updateMapper.getByTeacherUid(uid);
            dbTeacher.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dbTeacher.setName(dbSysUser.getRealname());
            dbTeacher.setSex(dbSysUser.getSex());
            updateMapper.updateByTeacherUid(dbTeacher);
        } else if (roleId == 3) {
            Student dbStudent = updateMapper.getByStudentUid(uid);
            dbStudent.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            dbStudent.setName(dbSysUser.getRealname());
            dbStudent.setSex(dbSysUser.getSex());
            updateMapper.updateByStudentUid(dbStudent);
        }
        sysUserRepository.save(dbSysUser);
    }

    /**
     * 根据ID删除用户信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        sysUserRepository.deleteById(id);
        updateMapper.deleteTeacher(id);
        updateMapper.deleteStudent(id);
    }

    /**
     * 更新个人密码
     * @param modifyPwdModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePwd(ModifyPwdModel modifyPwdModel) {
        // 获取根据Id获取用户信息
        SysUser dbUser = sysUserRepository.getReferenceById(modifyPwdModel.getUserId());
        // 判断输入旧密码是否正确
        String dbPwd = dbUser.getPassword();
        String usePwd = Md5Util.MD5(modifyPwdModel.getUsedPass());
        if(!usePwd.equals(dbPwd)){
            return false;
        }else {
            String newPwd = Md5Util.MD5(modifyPwdModel.getNewPass());
            dbUser.setPassword(newPwd);
            sysUserRepository.save(dbUser);
            return true;
        }

    }

}
