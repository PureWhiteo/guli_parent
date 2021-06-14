package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author wang
 * @since 2021-06-11
 */
public interface OrderService extends IService<Order> {

    String createOrders(String courseId, String memberId);
}
