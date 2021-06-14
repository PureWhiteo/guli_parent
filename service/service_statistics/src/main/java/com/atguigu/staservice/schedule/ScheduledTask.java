package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    public void task1(){
        System.out.println("自动任务");
    }

    //每天一点执行,把前一天数据查询添加
    @Scheduled(cron = "0 0 1 * * ? ")
    public void task2(){
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),1)));
    }
}
