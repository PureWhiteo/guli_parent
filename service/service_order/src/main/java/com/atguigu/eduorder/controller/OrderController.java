package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-06-11
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    //生成订单的方法
    @PostMapping("createOrder/{courseId}")
    public R savaOrder(@PathVariable String courseId, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //创建订单返回订单号
        String orderNo = orderService.createOrders(courseId,memberId);
        System.out.println(orderNo);
        return R.ok().data("orderId",orderNo);
    }

    //根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item",order);
    }

    //根据课程id和用户id查询订单表的订单状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);
        int count = orderService.count(wrapper);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }

}

