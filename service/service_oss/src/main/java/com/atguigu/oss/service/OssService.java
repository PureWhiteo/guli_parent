package com.atguigu.oss.service;


import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    //上传头像到OSS
    String uploadFileAvatar(MultipartFile file);
}
