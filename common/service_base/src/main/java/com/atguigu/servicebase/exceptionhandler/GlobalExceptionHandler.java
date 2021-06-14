package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
//错误信息写到文件
public class GlobalExceptionHandler {

    //指定出现什么异常执行
    @ExceptionHandler(Exception.class)
    @ResponseBody
    //返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    //返回数据
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    //返回数据
    public R error(GuliException e){
        log.error(e.getMsg());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
