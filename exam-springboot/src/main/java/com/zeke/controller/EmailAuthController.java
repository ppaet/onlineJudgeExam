package com.zeke.controller;

import com.alibaba.fastjson.JSONObject;
import com.zeke.service.MailService;
import com.zeke.utils.Code;
import com.zeke.utils.result.ApiResult;
import com.zeke.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * 邮件发送
 */
@RestController
@RequestMapping("/api/auth")
public class EmailAuthController {

    /**
     * 引入业务层依赖
     */
    @Autowired
    private MailService mailService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private EmailUtils emailUtils;

    @PostMapping("/send_email")
    public ApiResult<Object> send_email(@RequestBody JSONObject param) {
        String toEmail = param.getString("toEmail");
        ApiResult<Object> result = new ApiResult<>();
        String code = emailUtils.generateValidateCodeString(6);
        String text;
        if ("register".equals(param.getString("type"))) {
            text = "<h1>" +
                    "            亲爱的," +
                    "        </h1>" +
                    "        <h3>" +
                    "            欢迎来到 <b>在线考试系统</b>!" +
                    "        </h3>" +
                    "        <p>" +
                    "            您正在注册账号，您的验证码为：<b>%s</b>，赶快去完善注册信息吧！！！" +
                    "        </p>" +
                    "        <p>感谢您的支持和理解！</p>" +
                    "        <p>来自：在线考试系统！</p>" +
                    "        <p>验证码五分钟后过期！</p>";
        } else {
            text = "<h1>" +
                    "            亲爱的," +
                    "        </h1>" +
                    "        <h3>" +
                    "            欢迎来到 <b>在线考试系统</b>!" +
                    "        </h3>" +
                    "        <p>" +
                    "            您正在修改密码，您的验证码为：<b>%s</b>" +
                    "        </p>" +
                    "        <p>感谢您的支持和理解！</p>" +
                    "        <p>来自：在线考试系统！</p>" +
                    "        <p>验证码五分钟后过期！</p>";
        }
        boolean flag = mailService.sendMail(toEmail, code, text);
        if (flag) {
            // 将生成的验证码保存到Redis中并设置有效期五分钟
            redisTemplate.opsForValue().set(toEmail, code,
                    5, TimeUnit.MINUTES);
        }
        result.setCode(flag ? Code.SAVA_OK : Code.SAVA_ERR);
        result.setMsg(flag ? "邮件发送成功！" : "邮箱发送失败，请检查是否输入正确!");
        return result;
    }
}