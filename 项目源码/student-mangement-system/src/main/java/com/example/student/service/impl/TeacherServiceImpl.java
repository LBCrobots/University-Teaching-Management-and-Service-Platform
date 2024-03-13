package com.example.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.student.domain.Teacher;
import com.example.student.repository.TeacherRepository;
import com.example.student.service.ITeacherService;
import com.example.student.service.dto.TeacherQueryCriteria;
import com.example.utils.PageUtil;
import com.example.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：教师信息业务接口实现类*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements ITeacherService {

    private final TeacherRepository teacherRepository;


    /**
     * 获取教师列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(TeacherQueryCriteria queryCriteria, Pageable pageable) {
        Page<Teacher> page = teacherRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加教师信息
     * @param teacher
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTeacher(Teacher teacher) {
        Teacher dbTeacher = teacherRepository.save(teacher);
        return dbTeacher.getId()!=null;
    }

    /**
     * 获取教师信息
     * @param id
     * @return
     */
    @Override
    public Teacher getById(Long id) {
        return teacherRepository.findById(id).orElseGet(Teacher::new);
    }

    /**
     * 更新教师
     * @param teacher
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTeacher(Teacher teacher) {
        Teacher dbTeacher = teacherRepository.getReferenceById(teacher.getId());
        BeanUtil.copyProperties(teacher,dbTeacher, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        teacherRepository.save(dbTeacher);

    }

    /**
     * 根据ID删除教师信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }

    /**
     * 统计教师个数
     * @return
     */
    @Override
    public long getCount() {
        return teacherRepository.count();
    }
}
