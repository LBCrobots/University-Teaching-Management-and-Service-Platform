package com.example.student.service;

import com.example.student.domain.Scores;
import com.example.student.service.dto.CourseQueryCriteria;
import com.example.student.service.dto.ScoresQueryCriteria;

import com.example.student.vo.EchartsSeriesModel;
import com.example.student.vo.RegisterScoresModel;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**功能描述：成绩管理业务接口*/
public interface IScoresService {

    /**
     * 获取成绩列表
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(ScoresQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 登记班级学科成绩
     * @param scoresModel
     * @return
     */
    void registerScores(RegisterScoresModel scoresModel);

    @Transactional(rollbackFor = Exception.class)
    boolean addCourseSelect(Scores scores);

    /**
     * 更新成绩
     * @param scores
     */
    void editScores(Scores scores);

    /**
     * 删除成绩
     * @param id
     */
    void deleteById(Long id);

    /**
     * 统计班级科目成绩
     * @param courseId
     * @param gradeClassId
     * @return
     */
    List<EchartsSeriesModel> getScoreCensus(Long courseId, Long gradeClassId);

    /**
     * 班级学科成绩对比
     * @param courseId
     * @return
     */
    HashMap<String, Object> getScoresContrastCensus(Long courseId);

    /**
     * 所有学科成绩对比
     * @return
     */
    HashMap<String, Object> getAllSubjectScoreContrast();

    Object getStudentScoresList(Long studentId, ScoresQueryCriteria queryCriteria, Pageable pageable);

    Object getStudentsByTeacherCourses(Long uid, ScoresQueryCriteria queryCriteria, Pageable pageable);
}
