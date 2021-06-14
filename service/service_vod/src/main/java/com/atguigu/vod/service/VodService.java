package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    String uploadVideoAly(MultipartFile file);

    void removeMoreAlyVideo(List<String> videoList);
}
