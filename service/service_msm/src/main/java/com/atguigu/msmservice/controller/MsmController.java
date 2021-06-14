package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){
        //从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        boolean isSend = msmService.send(param,phone);
        if(isSend){
            //发送成功，把发送成功的验证码放入redis
            //设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else{
            return R.error().message("短信发送失败");
        }
    }
}
