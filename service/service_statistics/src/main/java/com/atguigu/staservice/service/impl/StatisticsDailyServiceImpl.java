package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.UcenterMember;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author wang
 * @since 2021-06-12
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;
    @Override
    public void registerCount(String day) {
        //添加先删除
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);
        R r = ucenterClient.countRegister(day);
        Integer countRegister = (Integer) r.getData().get("countRegister");
        //添加到数据库
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(countRegister);
        statisticsDaily.setDateCalculated(day);
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO
        statisticsDaily.setCourseNum(courseNum);
        statisticsDaily.setLoginNum(loginNum);
        statisticsDaily.setVideoViewNum(videoViewNum);

        baseMapper.insert(statisticsDaily);
    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);
        //要求json数组，对应后端list集合，需要日期list和数量list
        List<String> dateList = new ArrayList<>();
        List<Integer> numList = new ArrayList<>();
        for (StatisticsDaily statisticsDaily : staList) {
            dateList.add(statisticsDaily.getDateCalculated());
            switch (type){
                case "login_num":
                    numList.add(statisticsDaily.getLoginNum());
                    break;
                case "register_num":
                    numList.add(statisticsDaily.getRegisterNum());
                    break;
                case "video_view_num":
                    numList.add(statisticsDaily.getVideoViewNum());
                    break;
                case "course_num":
                    numList.add(statisticsDaily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",dateList);
        map.put("numDataList",numList);
        return map;
    }
}
