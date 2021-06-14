package com.atguigu.serviceedu.controller;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.client.VodClient;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-06-03
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;
    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
//        vodClient.removeAlyVideo("id");
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    // TODO 后面这个方法需要完善：删除小节时候，同时把里面视频删除
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据小节id获取视频id，调用方法实现视频删除，阿里云视频点播的视频
        EduVideo eduVideo = videoService.getById(id);
        String videoId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoId)){
            vodClient.removeAlyVideo(videoId);
        }

        //删除
        videoService.removeById(id);
        return R.ok();
    }

    //修改小节 TODO
}

