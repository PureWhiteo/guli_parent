package com.atguigu.educenter.service;

import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author wang
 * @since 2021-06-08
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countRegisterDay(String day);
}
