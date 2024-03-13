package com.example.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.student.domain.Course;

import com.example.student.repository.CourseRepository;
import com.example.student.service.ICourseService;
import com.example.student.service.dto.CourseQueryCriteria;
import com.example.utils.PageUtil;
import com.example.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：课程信息业务接口实现类*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements ICourseService {

    private final CourseRepository courseRepository;

    /**
     * 获取课程列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(CourseQueryCriteria queryCriteria, Pageable pageable) {
        Page<Course> page = courseRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加课程信息
     * @param course
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCourse(Course course) {
        Course dbCourse = courseRepository.save(course);
        return dbCourse.getId()!=null;
    }

    /**
     * 根据id获取课程信息
     * @param id
     * @return
     */
    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseGet(Course::new);
    }

    /**
     * 更新课程信息
     * @param course
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCourse(Course course) {
        Course dbCourse = courseRepository.getReferenceById(course.getId());
        BeanUtil.copyProperties(course,dbCourse, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        courseRepository.save(dbCourse);
    }

    /**
     * 根据id删除课程信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * 获取所有课程
     * @return
     */
    @Override
    public List<Course> queryAll() {
        return courseRepository.findAll();
    }

    /**
     * 统计课程门数
     * @return
     */
    @Override
    public long getCount() {
        return courseRepository.count();
    }
}
