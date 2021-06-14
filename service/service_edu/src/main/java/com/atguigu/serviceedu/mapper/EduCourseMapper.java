package com.atguigu.serviceedu.mapper;

import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.frontvo.CourseWebVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author wang
 * @since 2021-06-03
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    public CoursePublishVo getPublishCourseInfo(String course);
    public CourseWebVo getBaseCourseInfo(String courseId);
}
