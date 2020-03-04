package com.it.cloud.common.constants;

/**
 * <p>
 * JOB常量类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-25
 */
public class JobConstant {
    /**
     * JobDataMap 参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";


    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
