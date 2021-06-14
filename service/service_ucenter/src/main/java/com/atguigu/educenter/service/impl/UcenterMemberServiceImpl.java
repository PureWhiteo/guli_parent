package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author wang
 * @since 2021-06-08
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();
        //手机号和密码非空
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"登录失败");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if(ucenterMember==null){
            throw new GuliException(20001,"手机号不存在");
        }
        //判断密码
        if(!MD5.encrypt(password).equals(ucenterMember.getPassword())){
            throw new GuliException(20001,"密码错误");
        }
        if(ucenterMember.getIsDisabled()){
            throw new GuliException(20001,"用户被禁用");
        }
        //登录成功，生成token字符串
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return jwtToken;
    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)||StringUtils.isEmpty(nickname)||StringUtils.isEmpty(code)){
            throw new GuliException(20001,"注册失败");
        }
        //判断验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new GuliException(20001,"验证码错误");
        }
        //手机号不能重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"该手机号已存在");
        }
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://pic3.zhimg.com/v2-c2c2dd8b7b7cc609c1a81f65308ea30d_xl.jpg");
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
