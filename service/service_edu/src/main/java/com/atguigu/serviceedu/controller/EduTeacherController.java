package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQuery;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-05-30
 */
//创建对象，返回数据
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    //注入
    @Autowired
    private EduTeacherService teacherService;

    //查询Get
    //添加Post
    //修改Put
    //删除Delete
    //1.查询讲师表所有数据
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    //2.逻辑删除讲师的方法
    //swagger测试
    //postman
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //3.分页查询
    //current当前页
    //limit 每页记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit){
        //page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        teacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
//        Map map = new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);
//        try {
//            int a = 1/0;
//        } catch (Exception e) {
//            throw new GuliException(20001,"执行了自定义异常");
//        }
        return R.ok().data("total",total).data("rows",records);
    }

    //4.条件查询带分页
    //RequestBody 用json传数据
    //ResponseBody 返回json数据
//    @GetMapping("pageTeacherCondition/{current}/{limit}")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询  动态sql
        //判断条件值是否为空，不为空则拼接
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }
        wrapper.orderByDesc("gmt_create");
        teacherService.page(pageTeacher,wrapper);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

    //5.添加讲师
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //根据讲师ID
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //讲师修改功能
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }
}

