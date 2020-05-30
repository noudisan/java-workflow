package com.ztt.activiti;

import java.time.LocalTime;

/**
 * Created by zhoutaotao on 2020/3/27.
 */
public class Test {
    public static void main(String[] args) {
        String[] time = "01:00,02:00".split(",");
        int current = getCurrentHourMinute();
        System.out.println(current);
        System.out.println(getHourMinute(time[0]));
        System.out.println(getHourMinute(time[1]));
        if (current <getHourMinute(time[0]) || current > getHourMinute(time[1])) {
            System.out.println("aaaa");

            return;
        }

    }


    private static int getCurrentHourMinute() {
        int hour = 0;
        int minute = 0;
        return hour * 60 + minute;
    }


    public static int getHourMinute(String hourMinuteStr) {
        try {
            int hour = Integer.valueOf(hourMinuteStr.split(":")[0]);
            int minute = Integer.valueOf(hourMinuteStr.split(":")[1]);

            return hour * 60 + minute;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
