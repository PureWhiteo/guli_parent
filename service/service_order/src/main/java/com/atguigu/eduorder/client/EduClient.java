package com.atguigu.eduorder.client;

import com.atguigu.commonutils.CourseWebVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface EduClient {
    @PostMapping("/eduservice/coursefront/getCourseInfoOrder/{id}")
    public CourseWebVo getCourseInfoOrder(@PathVariable("id") String id);
}
