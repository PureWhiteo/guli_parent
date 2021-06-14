package com.atguigu.serviceedu.client;

import org.springframework.stereotype.Component;

@Component
public class OrderClientImpl implements OrderClient {
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
