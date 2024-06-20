package com.example.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.student.domain.Teacher;
import com.example.student.repository.TeacherMapper;
import com.example.student.repository.TeacherRepository;
import com.example.student.service.ITeacherService;
import com.example.student.service.dto.TeacherQueryCriteria;
import com.example.utils.PageUtil;
import com.example.utils.QueryHelp;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**功能描述：教师信息业务接口实现类*/
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements ITeacherService {

    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;


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
        log.info("teacherId是:{}",teacher.getUid());
        List<HashMap<String, Object>> courseIdList = teacherMapper.findCourseIdByTeacherUid(teacher.getUid());
        log.info("courseIdList是:{}",courseIdList);
        boolean courseAlreadySelected = courseIdList.stream()
                .anyMatch(item -> {
                    Long existingCourseId = (Long) item.get("course_id");
                    log.info("Comparing {} with {}", existingCourseId, teacher.getCourse().getId());
                    return existingCourseId.equals(teacher.getCourse().getId());
                });
        if (courseAlreadySelected) {
            return false;
        }
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

    /**
     * 获取学生个人课程列表数据
     *
     * @param uid     老师个人信息
     * @param queryCriteria 课程查询条件
     * @param pageable      分页信息
     * @return 老师个人信息列表数据
     */
    public Object getTeacherPersonalList(Long uid, TeacherQueryCriteria queryCriteria, Pageable pageable) {
        Specification<Teacher> specification = (root, query, criteriaBuilder) -> {
            Predicate teacherPredicate = criteriaBuilder.equal(root.get("uid"), uid);
            Predicate coursePredicate = QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder);
            return criteriaBuilder.and(teacherPredicate, coursePredicate);
        };
        Page<Teacher> page = teacherRepository.findAll(specification, pageable);
        return PageUtil.toPage(page);
    }
}
