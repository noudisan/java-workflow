package com.it.cloud.common.base;

import com.it.cloud.common.enums.ErrorEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * API接口的基础返回类
 *
 * @author 司马缸砸缸了
 * @version 1.0
 * @since 2019-07-12
 */

@ApiModel(value = "Result", description = "API接口的返回对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
// 使用chain属性，setter方法返回当前对象
@Accessors(chain = true)
public class Result<T> implements Serializable {
    /**
     * 是否成功
     */
    @ApiModelProperty(value = "是否成功", name = "success", example = "true", required = true)
    private boolean success;

    /**
     * 说明
     */
    @ApiModelProperty(value = "返回的详细说明", name = "msg", example = "成功")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回的数据", name = "data")
    private T data;

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static <T> Result ok() {
        return new Result(true, "请求成功");
    }

    public static <T> Result ok(T data) {
        return new Result(true, "请求成功").setData(data);
    }

    public static <T> Result ok(T data, String msg) {
        return new Result(true, msg).setData(data);
    }

    public static <T> Result error() {
        return new Result(false, ErrorEnum.UNKNOWN.getMsg());
    }

    public static <T> Result error(String msg) {
        return new Result(false, msg);
    }

    public static <T> Result error(String msg, T data) {
        return new Result(false, msg).setData(data);
    }

    public static <T> Result error(ErrorEnum errorEnum) {
        return new Result(false, errorEnum.getMsg());
    }
}
