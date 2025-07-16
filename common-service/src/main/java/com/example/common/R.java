package com.example.common;

import com.example.common.enums.ResultCodeEnum;
import lombok.Data;


@Data
public class R<T> {
    private String code;
    private String msg;
    private T data;

    public static <T> R<T> success() {
        R<T> tResult = new R<T>();
        tResult.setCode(ResultCodeEnum.SUCCESS.code);
        tResult.setMsg(ResultCodeEnum.SUCCESS.msg);
        return tResult;
    }

    public static <T>R<T> success(T data) {
        R<T> tResult = new R<T>();
        tResult.setData(data);
        tResult.setCode(ResultCodeEnum.SUCCESS.code);
        tResult.setMsg(ResultCodeEnum.SUCCESS.msg);
        return tResult;
    }

    public static <T>R<T> error() {
        R<T> tResult = new R<T>();
        tResult.setCode(ResultCodeEnum.SYSTEM_ERROR.code);
        tResult.setMsg(ResultCodeEnum.SYSTEM_ERROR.msg);
        return tResult;
    }

    public static <T>R<T> error(String code, String msg) {
        R<T> tResult = new R<T>();
        tResult.setCode(code);
        tResult.setMsg(msg);
        return tResult;
    }

    public static <T>R<T> error(ResultCodeEnum resultCodeEnum) {
        R<T> tResult = new R<T>();
        tResult.setCode(resultCodeEnum.code);
        tResult.setMsg(resultCodeEnum.msg);
        return tResult;
    }

}
