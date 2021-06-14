package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author wang
 * @since 2021-06-11
 */
public interface PayLogService extends IService<PayLog> {

    Map createNative(String orderNo);
    //根据订单号查询支付状态
    Map<String, String> queryPayStatus(String orderNo);
    //向支付表添加记录，更新订单状态
    void updateOrderStatus(Map<String, String> map);
}
