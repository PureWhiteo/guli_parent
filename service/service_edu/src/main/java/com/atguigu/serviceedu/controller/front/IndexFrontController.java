package com.atguigu.serviceedu.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;
    //查询前8条热门课程，查询前4条讲师
    @GetMapping("index")
    public R index(){
        QueryWrapper<EduCourse> wrapper1 = new QueryWrapper<>();
        wrapper1.orderByDesc("id");
        wrapper1.last("limit 8");
        List<EduCourse> eduList = courseService.list(wrapper1);

        QueryWrapper<EduTeacher> wrapper2 = new QueryWrapper<>();
        wrapper2.orderByDesc("id");
        wrapper2.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(wrapper2);

        return R.ok().data("eduList",eduList).data("teacherList",teacherList);
    }
}
