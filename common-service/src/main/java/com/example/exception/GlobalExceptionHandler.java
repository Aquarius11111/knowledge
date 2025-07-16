package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice(basePackages="com.example.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();


    //统一异常处理@ExceptionHandler,主要用于Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json串
    public R error(HttpServletRequest request, Exception e){
        log.error("异常信息：",e);
        return R.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody//返回json串
    public R customError(HttpServletRequest request, CustomException e){
        return R.error(e.getCode(), e.getMsg());
    }
}
