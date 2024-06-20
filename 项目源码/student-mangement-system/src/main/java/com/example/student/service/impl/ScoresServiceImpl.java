package com.example.student.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.base.BaseResult;
import com.example.student.domain.Course;
import com.example.student.domain.GradeClass;
import com.example.student.domain.Scores;
import com.example.student.domain.Student;
import com.example.student.repository.CourseScoresMapper;
import com.example.student.repository.ScoresRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.repository.UpdateMapper;
import com.example.student.service.IScoresService;
import com.example.student.service.dto.CourseQueryCriteria;
import com.example.student.service.dto.ScoresQueryCriteria;
import com.example.student.vo.BarEchartsSeriesModel;
import com.example.student.vo.EchartsSeriesModel;
import com.example.student.vo.RegisterScoresModel;
import com.example.utils.PageUtil;
import com.example.utils.QueryHelp;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



/**功能描述：成绩管理业务接口实现类*/
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoresServiceImpl implements IScoresService {

    private final ScoresRepository scoresRepository;

    private final StudentRepository studentRepository;

    private final CourseScoresMapper courseScoresMapper;

    private final UpdateMapper updateMapper;
    /**
     * 获取成绩列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(ScoresQueryCriteria queryCriteria, Pageable pageable) {
        Page<Scores> page = scoresRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 登记班级学科成绩
     * @param scoresModel
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerScores(RegisterScoresModel scoresModel) {

        // 根据班级ID获取该班级下的所有学生
        List<Student> studentList = studentRepository.findAllByGradeClassId(scoresModel.getGradeClassId());

        for(Student student: studentList){
            // 根据课程ID和学生ID查询成绩信息
            Scores dbScores = scoresRepository.getCourseByCourseIdAndStudentId(scoresModel.getCourseId(),student.getId());
            if(dbScores==null){
                // 新增记录
                dbScores = new Scores();
                dbScores.setType("未批改");
                dbScores.setScore(0);
                dbScores.setRemarks("初始成绩");
                dbScores.setStudent(student);
                Course tempCourse = new Course();
                // 课程
                tempCourse.setId(scoresModel.getCourseId());
                dbScores.setCourse(tempCourse);
                // 班级
                GradeClass gradeClass = new GradeClass();
                gradeClass.setId(scoresModel.getGradeClassId());
                dbScores.setGradeClass(gradeClass);
                scoresRepository.save(dbScores);
            }

        }
    }


    /**
     * 学生选课
     * @Param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCourseSelect(Scores scores){
        Long studentId = scores.getStudent().getId();
        log.info("studentId是:{}",studentId);
        Long gradeClassId = scores.getGradeClass().getId();
        log.info("gradeClassId是:{}",gradeClassId);

        List<HashMap<String, Object>> courseIdList = courseScoresMapper.findCourseIdByStudentId(studentId);
        log.info("courseList有：{}",courseIdList);

        Course courseToAddId = scores.getCourse();
        log.info("courseToAdd是:{}",courseToAddId);

        boolean courseAlreadySelected = courseIdList.stream()
                .anyMatch(item -> {
                    Long existingCourseId = (Long) item.get("course_id");
                    log.info("Comparing {} with {}", existingCourseId, courseToAddId.getId());
                    return existingCourseId.equals(courseToAddId.getId());
                });

        if (courseAlreadySelected) {
            return false;
        }
        if (gradeClassId == 0 || studentId == 0){
            return false;
        }
        scores.setScore(0);
        scores.setType("未批改");
        scores.setRemarks("初始成绩");
        scoresRepository.save(scores);
        courseScoresMapper.addGradeClassId(studentId, gradeClassId);
        return true;
    }

    /**
     * 更新成绩
     * @param scores
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editScores(Scores scores) {
        Scores dbScores = scoresRepository.getReferenceById(scores.getId());
        dbScores.setType("已批改");
        BeanUtil.copyProperties(scores,dbScores, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        scoresRepository.save(dbScores);

    }

    /**
     * 删除成绩信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        scoresRepository.deleteById(id);
    }

    /**
     * 统计班级科目成绩
     * @param courseId
     * @param gradeClassId
     * @return
     */
    @Override
    public List<EchartsSeriesModel> getScoreCensus(Long courseId, Long gradeClassId) {
        List<EchartsSeriesModel> data = new ArrayList<>();
        // 根据班级ID和课程ID获取所有成绩信息
        List<Scores> scoresList = scoresRepository.findAllByCourseIdAndGradeClassId(courseId,gradeClassId);

        // 成绩优秀人数
        AtomicInteger excellentNums= new AtomicInteger();
        EchartsSeriesModel excellentEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩是良的人数
        AtomicInteger goodNums = new AtomicInteger();
        EchartsSeriesModel goodEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩一般的人数
        AtomicInteger commonNums = new AtomicInteger();
        EchartsSeriesModel commonEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩差的人数
        AtomicInteger badNums = new AtomicInteger();
        EchartsSeriesModel badEchartsSeriesModel= new EchartsSeriesModel();
        // 成绩不及格的人数
        AtomicInteger failNums = new AtomicInteger();
        EchartsSeriesModel failEchartsSeriesModel= new EchartsSeriesModel();
        scoresList.stream().forEach(item-> {
            // 统计优秀人数
            if(item.getScore()>=90){
                excellentNums.getAndIncrement();
                excellentEchartsSeriesModel.setName("优");
                excellentEchartsSeriesModel.setValue(excellentNums.intValue());
             // 统计成绩是良好的人数
            }else if(item.getScore()>=80&&item.getScore()<90) {
                goodNums.getAndIncrement();
                goodEchartsSeriesModel.setName("良");
                goodEchartsSeriesModel.setValue(goodNums.intValue());
             // 统计成绩一般的人数
            }else if(item.getScore()>=70&&item.getScore()<80) {
                commonNums.getAndIncrement();
                commonEchartsSeriesModel.setName("一般");
                commonEchartsSeriesModel.setValue(commonNums.intValue());
                // 统计成绩较差的人数
            }else if(item.getScore()>=60&&item.getScore()<70) {
                badNums.getAndIncrement();
                badEchartsSeriesModel.setName("较差");
                badEchartsSeriesModel.setValue(badNums.intValue());
            }else {
                failNums.getAndIncrement();
                failEchartsSeriesModel.setName("不及格");
                failEchartsSeriesModel.setValue(failNums.intValue());
            }
        });

        // 优秀的人数
        if(excellentNums.intValue()!=0){
            data.add(excellentEchartsSeriesModel);
        }
        // 良好的人数
        if(goodNums.intValue()!=0){
            data.add(goodEchartsSeriesModel);
        }
        // 一般的人数
        if(commonNums.intValue()!=0){
            data.add(commonEchartsSeriesModel);
        }
        // 较差的人数
        if(badNums.intValue()!=0){
            data.add(badEchartsSeriesModel);
        }
        // 不及格的人数
        if(failNums.intValue()!=0){
            data.add(failEchartsSeriesModel);
        }

        return data;
    }

    /**
     * 班级学科成绩对比
     * @param courseId
     * @return
     */
    @Override
    public HashMap<String, Object> getScoresContrastCensus(Long courseId) {
        List<BarEchartsSeriesModel> barEchartsSeriesList = new ArrayList<>();
        // 获取该学科下的所有成绩记录
        List<Scores> scoresList = scoresRepository.findAllByCourseId(courseId);
        /*log.info("scoresList:{}",scoresList);*/
        // 统计方法同时统计同组的最大值、最小值、计数、求和、平均数信息
        HashMap<GradeClass, DoubleSummaryStatistics> resultGradeClass = scoresList.stream()
                .collect(Collectors.groupingBy(Scores::getGradeClass, HashMap::new, Collectors.summarizingDouble(Scores::getScore)));
        // 平均成绩
        List<Double> averageList = new ArrayList<>();
        // 最高成绩
        List<Double> maxList = new ArrayList<>();
        // 最低成绩
        List<Double> minList = new ArrayList<>();
        // 班级总人数
        List<Double> countList = new ArrayList<>();
        // 班级学科总成绩
        List<Double> sumList = new ArrayList<>();

        // 横坐标
        List<String> categoryList = new ArrayList<>();
        resultGradeClass.forEach((k, v) -> {
            // 班级名称
            categoryList.add(k.getName());
            // 平均成绩,保留两位小数
            v.getAverage();
            BigDecimal bigDecimal = new BigDecimal(v.getAverage());
           double average = bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            averageList.add(average);
            // 最高成绩
            v.getMax();
            maxList.add(v.getMax());
            // 最低成绩
            v.getMin();
            minList.add(v.getMin());
            // 班级总人数
            countList.add((double)v.getCount());
            // 总成绩
            sumList.add(v.getSum());
        });

        // 平均成绩
        BarEchartsSeriesModel averageBarEchartsSeriesModel = new BarEchartsSeriesModel();
        averageBarEchartsSeriesModel.setData(averageList);
        averageBarEchartsSeriesModel.setType("bar");
        averageBarEchartsSeriesModel.setName("平均成绩");
        barEchartsSeriesList.add(averageBarEchartsSeriesModel);
        // 最高成绩
        BarEchartsSeriesModel maxBarEchartsSeriesModel = new BarEchartsSeriesModel();
        maxBarEchartsSeriesModel.setData(maxList);
        maxBarEchartsSeriesModel.setType("bar");
        maxBarEchartsSeriesModel.setName("最高成绩");
        barEchartsSeriesList.add(maxBarEchartsSeriesModel);
        // 最低成绩
        BarEchartsSeriesModel minBarEchartsSeriesModel = new BarEchartsSeriesModel();
        minBarEchartsSeriesModel.setData(minList);
        minBarEchartsSeriesModel.setType("bar");
        minBarEchartsSeriesModel.setName("最低成绩");
        barEchartsSeriesList.add(minBarEchartsSeriesModel);
        // 班级总人数
        BarEchartsSeriesModel countBarEchartsSeriesModel = new BarEchartsSeriesModel();
        countBarEchartsSeriesModel.setData(countList);
        countBarEchartsSeriesModel.setType("bar");
        countBarEchartsSeriesModel.setName("总人数");
        barEchartsSeriesList.add(countBarEchartsSeriesModel);
        // 班级学科总成绩
        BarEchartsSeriesModel sumBarEchartsSeriesModel = new BarEchartsSeriesModel();
        sumBarEchartsSeriesModel.setData(sumList);
        sumBarEchartsSeriesModel.setType("bar");
        sumBarEchartsSeriesModel.setName("总成绩");
        barEchartsSeriesList.add(sumBarEchartsSeriesModel);

        // 定义返回对象
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryList",categoryList);
        resultMap.put("barEchartsSeriesList",barEchartsSeriesList);
        return resultMap;
    }

    /**
     * 所有学科成绩对比
     * @return
     */
    @Override
    public HashMap<String, Object> getAllSubjectScoreContrast() {
        List<BarEchartsSeriesModel> barEchartsSeriesList = new ArrayList<>();
        // 获取该学科下的所有成绩记录
        List<Scores> scoresList = scoresRepository.findAll();

        // 统计方法同时统计同组的最大值、最小值、计数、求和、平均数信息
        HashMap<Course, DoubleSummaryStatistics> resultGradeClass = scoresList.stream()
                .collect(Collectors.groupingBy(Scores::getCourse, HashMap::new, Collectors.summarizingDouble(Scores::getScore)));
        // 平均成绩
        List<Double> averageList = new ArrayList<>();
        // 最高成绩
        List<Double> maxList = new ArrayList<>();
        // 最低成绩
        List<Double> minList = new ArrayList<>();
        // 总人数
        List<Double> countList = new ArrayList<>();
        // 横坐标
        List<String> categoryList = new ArrayList<>();
        resultGradeClass.forEach((k, v) -> {
            // 学科名称
            categoryList.add(k.getCoursename());
            // 平均成绩,保留两位小数
            v.getAverage();
            BigDecimal bigDecimal = new BigDecimal(v.getAverage());
            double average = bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
            averageList.add(average);
            // 最高成绩
            v.getMax();
            maxList.add(v.getMax());
            // 最低成绩
            v.getMin();
            minList.add(v.getMin());
            // 总人数
            countList.add((double)v.getCount());

        });

        // 平均成绩
        BarEchartsSeriesModel averageBarEchartsSeriesModel = new BarEchartsSeriesModel();
        averageBarEchartsSeriesModel.setData(averageList);
        averageBarEchartsSeriesModel.setType("bar");
        averageBarEchartsSeriesModel.setName("平均成绩");
        barEchartsSeriesList.add(averageBarEchartsSeriesModel);
        // 最高成绩
        BarEchartsSeriesModel maxBarEchartsSeriesModel = new BarEchartsSeriesModel();
        maxBarEchartsSeriesModel.setData(maxList);
        maxBarEchartsSeriesModel.setType("bar");
        maxBarEchartsSeriesModel.setName("最高成绩");
        barEchartsSeriesList.add(maxBarEchartsSeriesModel);
        // 最低成绩
        BarEchartsSeriesModel minBarEchartsSeriesModel = new BarEchartsSeriesModel();
        minBarEchartsSeriesModel.setData(minList);
        minBarEchartsSeriesModel.setType("bar");
        minBarEchartsSeriesModel.setName("最低成绩");
        barEchartsSeriesList.add(minBarEchartsSeriesModel);
        // 班级总人数
        BarEchartsSeriesModel countBarEchartsSeriesModel = new BarEchartsSeriesModel();
        countBarEchartsSeriesModel.setData(countList);
        countBarEchartsSeriesModel.setType("bar");
        countBarEchartsSeriesModel.setName("总人数");
        barEchartsSeriesList.add(countBarEchartsSeriesModel);

        // 定义返回对象
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("categoryList",categoryList);
        resultMap.put("barEchartsSeriesList",barEchartsSeriesList);
        return resultMap;
    }

    /**
     * 获取学生个人课程列表数据
     *
     * @param studentUId     学生ID
     * @param queryCriteria 课程查询条件
     * @param pageable      分页信息
     * @return 学生个人课程列表数据
     */
    public Object getStudentScoresList(Long studentUId, ScoresQueryCriteria queryCriteria, Pageable pageable) {
        // 构建查询条件
        Specification<Scores> specification = (root, query, criteriaBuilder) -> {
            // 添加学生ID的条件
            Predicate studentPredicate = criteriaBuilder.equal(root.get("student").get("uid"), studentUId);

            // 添加课程查询条件
            Predicate coursePredicate = QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder);

            // 组合学生ID条件和课程查询条件
            return criteriaBuilder.and(studentPredicate, coursePredicate);
        };

        // 查询学生个人课程列表数据
        Page<Scores> page = scoresRepository.findAll(specification, pageable);

        // 转换为通用分页格式并返回
        return PageUtil.toPage(page);
    }

    @Override
    public Object getStudentsByTeacherCourses(Long uid, ScoresQueryCriteria queryCriteria, Pageable pageable) {
        // 根据老师的 UID 获取老师教的所有课程的 ID 列表
        List<Long> teacherCourses = updateMapper.getCoursesByTeacherUid(uid);
        log.info("teacherCourses是：{}", teacherCourses);

        // 构建总的查询条件
        Specification<Scores> specification = (root, query, criteriaBuilder) -> {
            // 创建一个空的 Predicate，用于后续组合
            Predicate predicate = criteriaBuilder.conjunction();

            // 添加老师教授课程的查询条件
            Predicate coursePredicate = root.get("course").get("id").in(teacherCourses);

            // 添加课程其他查询条件
            Predicate otherCriteriaPredicate = QueryHelp.getPredicate(root, queryCriteria, criteriaBuilder);

            // 组合查询条件
            predicate = criteriaBuilder.and(coursePredicate, otherCriteriaPredicate);

            return predicate;
        };

        // 查询选修老师教授课程的学生列表
        Page<Scores> scoresPage = scoresRepository.findAll(specification, pageable);

        // 转换为通用分页格式并返回
        return PageUtil.toPage(scoresPage);
    }


}
