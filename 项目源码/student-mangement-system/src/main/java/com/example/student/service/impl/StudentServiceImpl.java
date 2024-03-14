package com.example.student.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.student.domain.Student;
import com.example.student.repository.StudentRepository;
import com.example.student.service.IStudentService;
import com.example.student.service.dto.StudentQueryCriteria;
import com.example.utils.PageUtil;
import com.example.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：学生信息业务接口实现类*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements IStudentService {

    private final StudentRepository studentRepository;


    /**
     * 获取学生列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(StudentQueryCriteria queryCriteria, Pageable pageable) {
        Page<Student> page = studentRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加学生信息
     * @param student
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addStudent(Student student) {
        Student dbStudent = studentRepository.save(student);
        return dbStudent.getId()!=null;
    }

    /**
     * 根据ID获取学生详情信息
     * @param id
     * @return
     */
    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id).orElseGet(Student::new);
    }

    /**
     * 更新学生信息
     * @param student
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editStudent(Student student) {
        Student dbStudent = studentRepository.getReferenceById(student.getId());
        BeanUtil.copyProperties(student,dbStudent, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        studentRepository.save(dbStudent);

    }

    /**
     * 根据ID删除学生信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    /**
     * 统计人数
     * @return
     */
    @Override
    public long getCount() {
        return studentRepository.count();
    }
}
