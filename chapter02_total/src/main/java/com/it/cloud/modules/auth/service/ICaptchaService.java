package com.it.cloud.modules.auth.service;


import java.awt.image.BufferedImage;

/**
 * 验证码
 *
 * @author 司马缸砸缸了
 * @version 1.0
 * @since 2019-07-12
 */
public interface ICaptchaService {

    /**
     * 获取验证码
     *
     * @param uuid
     * @return
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
