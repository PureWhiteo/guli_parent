package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtils.END_POINT;
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

// 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            InputStream inputStream = file.getInputStream();
            // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
            //文件名字唯一值
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-","");
            fileName = uuid+fileName;
            //2021/6/2
            //按日期创建文件夹
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath+"/" + fileName;
            ossClient.putObject(bucketName, fileName, inputStream);

// 关闭OSSClient。
            ossClient.shutdown();

            //把上传文件之后的文件路径返回
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
