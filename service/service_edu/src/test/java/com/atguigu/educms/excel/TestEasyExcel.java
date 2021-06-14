package com.atguigu.educms.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestEasyExcel {
    public static void main(String[] args) {
//        //实现Excel写操作
//        //1.设置写入文件夹地址和excel文件名称
//        String filename = "F:\\write.xlsx";
//
//        //2.调用easyexcel里面方法实现写操作
//        EasyExcel.write(filename,DemoData.class).sheet("学生").doWrite(getData());


        //实现excel读操作
        String filename = "F:\\write.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListen()).sheet().doRead();
    }

    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname(UUID.randomUUID().toString());
            list.add(data);
        }
        return list;
    }
}
