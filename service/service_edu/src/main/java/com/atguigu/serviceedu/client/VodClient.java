package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service-vod",fallback =  VodFileDegradeFeignClient.class)  //调用服务的名称
@Component
public interface VodClient {
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);

    //定义调用删除多个视频的方法
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoList);
}
