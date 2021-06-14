package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CourseInfoVo;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.frontvo.CourseFrontVo;
import com.atguigu.serviceedu.frontvo.CourseWebVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wang
 * @since 2021-06-03
 */
public interface EduCourseService extends IService<EduCourse> {
    String saveCourseInfo(CourseInfoVo courseInfoVo);
    CourseInfoVo getCourseInfo(String courseId);
    void updateCourseInfo(CourseInfoVo courseInfoVo);
    CoursePublishVo publishCourseInfo(String id);
    void removeCourse(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    CourseWebVo getBaseCourseInfo(String courseId);

}
