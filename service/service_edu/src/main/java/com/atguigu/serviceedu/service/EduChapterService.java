package com.atguigu.serviceedu.service;

import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wang
 * @since 2021-06-03
 */
public interface EduChapterService extends IService<EduChapter> {
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeChapterById(String courseId);
}
