package com.aug.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author querkecor
 * @date 2023/4/23
 */

@Data
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    private Result() {}

    public static<E> Result<E> success() {
        return build(null,ResultCodeEnum.SUCCESS);
    }

    public static<E> Result<E> success(E data) {
        return build(data,ResultCodeEnum.SUCCESS);
    }

    public static<E> Result<E> fail() {
        return build(null,ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    // 返回数据
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null)
            result.setData(data);
        return result;
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

}
