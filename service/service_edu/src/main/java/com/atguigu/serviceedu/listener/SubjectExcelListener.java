package com.atguigu.serviceedu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.excel.SubjectData;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
//    @Autowired
//    private EduSubjectService eduSubjectService;
    public EduSubjectService subjectService;
    public SubjectExcelListener(EduSubjectService subjectService){
        this.subjectService = subjectService;
    }
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData==null){
            throw new GuliException(20001,"文件数据为空");
        }
        //先判断一级分类是否重复
        EduSubject eduSubject = this.existOneSubject(subjectData.getOneSubjectName());
        if(eduSubject==null){  //没有这个一级分类
            eduSubject = new EduSubject();
            eduSubject.setParentId("0");
            eduSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(eduSubject);
        }
        String pid = eduSubject.getId();
        //添加二级分类
        EduSubject eduSubject1 = this.existTwoSubject(subjectData.getTwoSubjectName(), pid);
        if(eduSubject1==null){  //没有这个一级分类
            eduSubject1 = new EduSubject();
            eduSubject1.setParentId(pid);
            eduSubject1.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(eduSubject1);
        }
    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
