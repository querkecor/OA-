package com.aug.common.config.exception;

import com.aug.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author querkecor
 * @date 2023/4/30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AugException extends RuntimeException {

    private Integer code;// 状态码
    private String msg;// 描述信息

    public AugException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 接收枚举类型对象
     *
     * @param resultCodeEnum
     */
    public AugException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}

