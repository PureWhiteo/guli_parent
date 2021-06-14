package com.atguigu.educms.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;

import java.util.Map;

public class ExcelListen extends AnalysisEventListener<DemoData> {

    @Override
    public void invoke(DemoData data, AnalysisContext analysisContext) {
        System.out.println("**"+data);
    }
    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
