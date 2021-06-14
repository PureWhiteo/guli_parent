package com.atguigu.serviceedu.service.impl;

import com.alibaba.excel.EasyExcel;
        import com.atguigu.serviceedu.entity.EduSubject;
        import com.atguigu.serviceedu.entity.excel.SubjectData;
        import com.atguigu.serviceedu.entity.subject.OneSubject;
        import com.atguigu.serviceedu.entity.subject.TwoSubject;
        import com.atguigu.serviceedu.listener.SubjectExcelListener;
        import com.atguigu.serviceedu.mapper.EduSubjectMapper;
        import com.atguigu.serviceedu.service.EduSubjectService;
        import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
        import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
        import org.springframework.beans.BeanUtils;
        import org.springframework.stereotype.Service;
        import org.springframework.web.multipart.MultipartFile;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author wang
 * @since 2021-06-02
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void savaSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id","0");
        //一级分类
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper1);

        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapper2);
        List<OneSubject> finalSubjectList = new ArrayList<>();
        //封装一级分类
        for (int i = 0; i < oneSubjectList.size(); i++) {
            //得到oneSubjectList每个eduSubject对象
            EduSubject eduSubject = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
            BeanUtils.copyProperties(eduSubject,oneSubject);


            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for(int j = 0;j<twoSubjectList.size();j++){
                if(twoSubjectList.get(j).getParentId().equals(oneSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(twoSubjectList.get(j),twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoFinalSubjectList);
            finalSubjectList.add(oneSubject);
        }
        return finalSubjectList;
    }
}
