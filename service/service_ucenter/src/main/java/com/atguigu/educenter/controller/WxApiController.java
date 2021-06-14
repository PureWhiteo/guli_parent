package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;
    //1 生成微信扫描二维码
    @GetMapping("login")
    public String getWxCode() {
        //固定地址，后面拼接参数
//        String url = "https://open.weixin.qq.com/" +
//                "connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";

        // 微信开放平台授权baseUrl  %s相当于?代表占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch(Exception e) {
        }

        //设置%s里面值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );

        //重定向到请求微信地址里面
        return "redirect:"+url;
    }

    //2.获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code,String state) throws Exception {
            //获取code值，临时票据，类似验证码

            //用code请求微信固定地址，得到access_token  和openid
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,ConstantWxUtils.WX_OPEN_APP_ID,ConstantWxUtils.WX_OPEN_REDIRECT_URL,code);
            //请求拼接好的地址，得到两个返回的值.access_token和openid
            //使用httpclient
        String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
        //从accessTokenInfo字符串取值
        //把accessTokenInfo转换成map集合,json转换工具Gson
        Gson gson = new Gson();
        HashMap hashMap = gson.fromJson(accessTokenInfo, HashMap.class);
        String access_token = (String)hashMap.get("access_token");
        String openid = (String)hashMap.get("openid");

        //把扫描人信息添加数据库，若没有，则自动注册
        UcenterMember member = memberService.getOpenIdMember(openid);
        if(member==null){    //表中没数据
            //用access_token和openid 请求微信资源服务器，获取用户信息
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
            //发送请求
            String userInfo = HttpClientUtils.get(userInfoUrl);
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            String nickname = (String) userInfoMap.get("nickname");
            String headimgurl = (String) userInfoMap.get("headimgurl");
            member.setOpenid(openid);
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return "redirect:http://localhost:3000?token="+token;
        }
}
