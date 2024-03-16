package com.example.student.controller;

import com.example.base.BaseResult;
import com.example.exception.BadRequestException;
import com.example.student.domain.Course;
import com.example.student.repository.CourseScoresMapper;
import com.example.student.service.ICourseService;
import com.example.student.service.IScoresService;
import com.example.student.service.dto.CourseQueryCriteria;
import com.example.student.service.dto.ScoresQueryCriteria;
import com.example.utils.PageVo;
import com.example.utils.ResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**功能描述：课程信息前端控制器*/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("course")
public class CourseController {

    private final ICourseService courseService;

    /**
     * 获取课程列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(CourseQueryCriteria queryCriteria, PageVo pageVo){
        log.info("queryCriteria的内容是：{}",queryCriteria);
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        log.info("pageable的内容是：{}",pageable);
        return new ResponseEntity<>(courseService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加课程信息
     * @param course
     * @return
     */
    @PostMapping
    public BaseResult addCourse(@RequestBody Course course){
        boolean result= courseService.addCourse(course);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取课程详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        Course dbCourse = courseService.getById(id);
        return BaseResult.success(dbCourse);
    }

    /**
     * 更新课程信息
     * @param course
     * @return
     */
    @PutMapping
    public BaseResult editCourse(@RequestBody Course course){
        courseService.editCourse(course);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除课程信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        courseService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    /**
     * 获取所有课程信息
     * @param
     * @return
     */
    @GetMapping(value = "/all")
    public BaseResult getAll(){
        List<Course> list =  courseService.queryAll();
        List<ResultVo> result = list.stream().map(temp -> {
            ResultVo obj = new ResultVo();
            obj.setName(temp.getCoursename());
            obj.setId(temp.getId());
            return obj;
        }).collect(Collectors.toList());
        return BaseResult.success(result);
    }

}
