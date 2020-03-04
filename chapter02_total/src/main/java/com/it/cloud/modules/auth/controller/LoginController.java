package com.it.cloud.modules.auth.controller;

import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.it.cloud.common.base.BaseController;
import com.it.cloud.common.base.Result;
import com.it.cloud.common.enums.ErrorEnum;
import com.it.cloud.modules.auth.entity.UserEntity;
import com.it.cloud.modules.auth.entity.form.LoginForm;
import com.it.cloud.modules.auth.oauth2.OAuth2Realm;
import com.it.cloud.modules.auth.service.ICaptchaService;
import com.it.cloud.modules.auth.service.IUserService;
import com.it.cloud.modules.auth.service.IUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.it.cloud.modules.auth.oauth2.OAuth2Realm;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 * 登陆控制器
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-07-24
 */
@Api(value = "登陆控制器", tags = "登陆")
@Slf4j
@RestController
public class LoginController extends BaseController {

    @Autowired
    private ICaptchaService captchaService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserTokenService userTokenService;

    @Autowired
    private OAuth2Realm oAuth2Realm;

    @ApiOperation(value = "分页查询用户接口", notes = "条件，分页查询")
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = captchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @ApiOperation(value = "登陆", notes = "登陆")
    @PostMapping("/admin/login")
    public Result login(@RequestBody LoginForm form) {
        boolean captcha = captchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            // 验证码不正确
            return Result.error(ErrorEnum.CAPTCHA_WRONG);
        }

        // 用户信息
        UserEntity user = userService.getOne(Wrappers.<UserEntity>lambdaQuery()
                .eq(UserEntity::getUsername, form.getUsername()));
        if (user == null) {
            // 用户不存在
            return Result.error(ErrorEnum.LOGIN_FAIL);
        }
        //用户真实密码
        String password = new Sha256Hash(form.getPassword(), user.getSalt()).toHex();
        //校验密码
        if (user == null || !user.getPassword().equals(password)) {
            // 用户名或密码错误
            return Result.error(ErrorEnum.USERNAME_OR_PASSWORD_WRONG);
        }
        if (user.getStatus() != 0) {
            return Result.error("账号已被锁定，请联系管理员");
        }

        //生成token，并保存到redis
        return userTokenService.createToken(user.getId());
    }

    @ApiOperation(value = "注销", notes = "注销")
    @PostMapping("/admin/logout")
    public Result logout() {
        userTokenService.logout(getUserId());
        return Result.ok();
    }

    @ApiOperation(value = "生成密码", notes = "生成密码")
    @PostMapping("/password")
    public Result password(@RequestParam String password, @RequestParam String salt) {
        String pwd = new Sha256Hash(password, salt).toHex();
        return Result.ok(pwd);
    }

    @ApiOperation(value = "清除缓存", notes = "清除缓存")
    @GetMapping("/admin/clear")
    public void clearCache() {
        oAuth2Realm.clearCache();
    }

    @ApiOperation(value = "模拟身份信息（临时）", notes = "模拟身份信息")
    @GetMapping("/get_info")
    public Result getInfo(@RequestParam String token) {
        String info = "{\"name\":\"super_admin\",\"user_id\":\"1\",\"access\":[\"super_admin\",\"admin\"]," +
                "\"token\":\"super_admin\",\"avatar\":" +
                "\"https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png\"}";
        return Result.ok(info);
    }

}
