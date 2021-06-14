package com.atguigu.educenter.mapper;

import com.atguigu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author wang
 * @since 2021-06-08
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    Integer countRegisterDay(String day);
}
