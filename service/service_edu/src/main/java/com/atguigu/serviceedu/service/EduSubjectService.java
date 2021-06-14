package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author wang
 * @since 2021-06-02
 */
public interface EduSubjectService extends IService<EduSubject> {
    void savaSubject(MultipartFile file,EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();
}
