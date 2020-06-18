package com.imooc.activiti.activitidemo.interceptor;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 执行时间
 * @Author 胡浩
 * @Date 2019/8/20
 **/
public class DurationComandInterceptor extends AbstractCommandInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DurationComandInterceptor.class);

    @Override
    public <T> T execute(CommandConfig commandConfig, Command<T> command) {
        long start = System.currentTimeMillis();
        try {
            return this.getNext().execute(commandConfig, command);
        } finally {
            long duration = System.currentTimeMillis() - start;
            LOGGER.info("{} 执行时长 {} 毫秒", command.getClass().getSimpleName(), duration);
        }

    }
}
