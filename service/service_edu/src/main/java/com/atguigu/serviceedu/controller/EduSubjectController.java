package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.subject.OneSubject;
import com.atguigu.serviceedu.entity.subject.TwoSubject;
import com.atguigu.serviceedu.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-06-02
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传的文件，把文件内容读取出来
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //上传excel文件
        System.out.println(file);
        subjectService.savaSubject(file,subjectService);
        return R.ok();
    }

    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}

