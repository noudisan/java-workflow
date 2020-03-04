package com.it.cloud.activiti.entity;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-09-25
 */
public class Leave implements Serializable {

    private Integer days;

    private String reason;


    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
