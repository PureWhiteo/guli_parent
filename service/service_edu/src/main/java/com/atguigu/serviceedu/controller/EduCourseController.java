package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CourseInfoVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;
    //添加课程
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublish = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublish);
    }

    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    @GetMapping
    public R getCourseList(){
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }

    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }
}

