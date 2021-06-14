package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.CourseWebVo;
import com.atguigu.commonutils.UcenterMember;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author wang
 * @since 2021-06-11
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;
    @Override
    public String createOrders(String courseId, String memberId) {
        //根据用户id获取用户信息
        UcenterMember ucenter = ucenterClient.getUcenterById(memberId);
        //查询课程
        CourseWebVo course = eduClient.getCourseInfoOrder(courseId);
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());    //订单号
        order.setCourseId(courseId);
        order.setCourseCover(course.getAvatar());
        order.setCourseTitle(course.getTitle());
        order.setTeacherName(course.getTeacherName());
        order.setTotalFee(course.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenter.getMobile());
        order.setNickname(ucenter.getNickname());
        order.setStatus(0);
        order.setPayType(1);

        baseMapper.insert(order);
        //返回订单号
        return order.getOrderNo();
    }
}
