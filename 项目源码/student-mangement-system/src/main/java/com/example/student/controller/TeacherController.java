package com.example.student.controller;

import com.example.base.BaseResult;
import com.example.exception.BadRequestException;
import com.example.student.domain.Teacher;
import com.example.student.service.ITeacherService;
import com.example.student.service.dto.ScoresQueryCriteria;
import com.example.student.service.dto.TeacherQueryCriteria;
import com.example.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：教师信息前端控制器*/
@RestController
@RequestMapping("teacher")
public class TeacherController {

    private final ITeacherService teacherService;

    public TeacherController(ITeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * 获取教师列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(TeacherQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        return new ResponseEntity<>(teacherService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加教师信息
     * @param teacher
     * @return
     */
    @PostMapping
    public BaseResult addTeacher(@RequestBody Teacher teacher){
        boolean result= teacherService.addTeacher(teacher);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取教师详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        Teacher dbTeacher = teacherService.getById(id);
        return BaseResult.success(dbTeacher);
    }

    /**
     * 更新教师信息
     * @param teacher
     * @return
     */
    @PutMapping
    public BaseResult editTeacher(@RequestBody Teacher teacher){
        teacherService.editTeacher(teacher);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除教师信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        teacherService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    /**
     * 获取老师个人课程列表数据
     *
     * @param uid     老师个人信息
     * @param queryCriteria 课程查询条件
     * @param pageVo     分页信息
     * @return 老师个人课程列表数据
     */
    @GetMapping("/getTeacherPersonalList")
    public ResponseEntity<Object> getTeacherPersonalList(Long uid, TeacherQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(), Sort.Direction.DESC, "id");
        Object result = teacherService.getTeacherPersonalList(uid, queryCriteria, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
