package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author wang
 * @since 2021-06-11
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayLogService payLogService;
    @Override
    public Map createNative(String orderNo) {
        try{
            //根据订单号查询信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);
            Map m = new HashMap();
            //使用map设置生成二维码所需参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderNo);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            m.put("trade_type", "NATIVE");
            //发送httpclient请求，传递参数xml格式，微信支付提供的固定地址
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置xml格式
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            httpClient.setHttps(true);
            httpClient.post();
            //得到结果，返回
            String content = httpClient.getContent();//结果
            //返回内容为xml格式，把xml转换为map
            Map<String,String> resultMap = WXPayUtil.xmlToMap(content);  //二维码地址
            Map map = new HashMap();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));
            return map;
        }catch (Exception e){
            throw new GuliException(20001,"生成二维码失败");
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //从map获取订单号
        String orderNo = map.get("out_trade_no");
        //根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        //更新订单表状态
        if(order.getStatus()==1){
            return;
        }
        //controller已经判断state
        order.setStatus(1);
        orderService.updateById(order);

        //向支付表添加记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));  //其他属性

//        payLog.setOrderNo(orderNo);  //订单号
//        payLog.setPayTime(new Date()); //订单完成时间
//        payLog.setPayType(1);//支付类型 1微信
//        payLog.setTotalFee(order.getTotalFee());//总金额(分)
//
//        payLog.setTradeState(map.get("trade_state"));//支付状态
//        payLog.setTransactionId(map.get("transaction_id")); //流水号
//        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);
    }
}
