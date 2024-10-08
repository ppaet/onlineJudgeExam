package com.zeke.utils;

import com.zeke.utils.result.TempResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailUtils {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public String generateValidateCodeString(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    public TempResult emailCheck(String code, String toEmail) {
        ValueOperations<Object, Object> redisFor = redisTemplate.opsForValue();
        String rightCode = (String) redisFor.get(toEmail);
        TempResult result = new TempResult();
        result.setFlag(true);
        if (rightCode == null) {
            result.setFlag(false);
            result.setMsg("验证码过期，请重试...");
            return result;
        }
        if (!rightCode.equals(code)) {
            result.setFlag(false);
            result.setMsg("验证码错误,请重试...");
            return result;
        }
        result.setMsg("邮箱验证通过...");
        return result;
    }
}
