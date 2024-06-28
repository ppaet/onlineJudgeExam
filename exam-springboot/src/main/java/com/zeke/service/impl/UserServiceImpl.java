package com.zeke.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zeke.service.UserService;
import com.zeke.bean.User;
import com.zeke.dao.UserDao;
import com.zeke.utils.Code;
import com.zeke.utils.JwtTokenUtil;
import com.zeke.utils.result.ApiResult;
import com.zeke.utils.result.TempResult;
import com.zeke.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public TempResult register(User user) {
        TempResult result = emailUtils.emailCheck(user.getEmailCode(), user.getEmail());
        if (!result.isFlag()) {
            return result;
        }
        if (user.getrId() != 1 && user.getrId() != 2) {
            result.setFlag(false);
            result.setMsg("注册角色错误");
            return result;
        }
        ArrayList<User> isExist = userDao.selectByEmail(user);
        if (!isExist.isEmpty()) {
            result.setFlag(false);
            result.setMsg("邮箱已被注册...");
            return result;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        Integer isSucceed = userDao.addUser(user);
        if (isSucceed != null && isSucceed != 0) {
            result.setMsg("注册成功！");
            result.setFlag(true);
        }
        return result;
    }

    @Override
    public ApiResult<JSONObject> login(User user) {
        ArrayList<User> userList = userDao.selectByEmail(user);
        ApiResult<JSONObject> apiResult = new ApiResult<>();
        if (userList.isEmpty()) {
            apiResult.setCode(Code.GET_ERR);
            apiResult.setMsg("账号不存在！");
        } else {
            User rightUser = userList.get(0);
            boolean isSucceed = encoder.matches(user.getPassword(), rightUser.getPassword());
            apiResult.setCode(isSucceed ? Code.GET_OK : Code.GET_ERR);
            JSONObject data = new JSONObject();
            // 返回token，并将用户信息存储到redis
            if (isSucceed) {
                JSONObject userJson = new JSONObject();
                userJson.put("uId", rightUser.getuId());
                userJson.put("username", rightUser.getUsername());
                String token = jwtTokenUtil.generateToken(userJson.toJSONString());
                String parserToken = jwtTokenUtil.parserToken(token);
                System.out.println(parserToken);
                data.put("token", token);
                redisTemplate.opsForValue().set("userId:" + rightUser.getuId(), rightUser);
            }
            apiResult.setData(data);
            apiResult.setMsg(isSucceed ? "登录成功！" : "密码错误！");
        }
        return apiResult;
    }

    public TempResult updatePassword(User user) {
        TempResult tempResult = emailUtils.emailCheck(user.getEmailCode(), user.getEmail());
        if (tempResult.isFlag()) {
            user.setPassword(encoder.encode(user.getPassword()));
            tempResult.setMsg(userDao.updateUser(user) != 0 ? "修改成功！" : Code.ERROR_MSG);
        }
        return tempResult;
    }

    @Override
    public ApiResult<Object> logout(String uId) {
        boolean isLogout = Boolean.TRUE.equals(redisTemplate.delete("userId:" + uId));
        return new ApiResult<>(isLogout ? Code.DELETE_OK : Code.DELETE_ERR, null,
                isLogout ? "退出成功！" : Code.ERROR_MSG);
    }

    @Override
    public ApiResult<Object> forgetpw(User user) {
        TempResult result = emailUtils.emailCheck(user.getEmailCode(), user.getEmail());
        if (!result.isFlag()) {
            return new ApiResult<>(Code.SAVA_ERR,null, "验证码错误");
        }
        ArrayList<User> isExist = userDao.selectByEmail(user);
        if (isExist.isEmpty()) {
            return new ApiResult<>(Code.SAVA_ERR,null, "账号不存在");
        }
        isExist.get(0).setPassword(encoder.encode(user.getPassword()));
        Integer isSucceed = userDao.updateUser(isExist.get(0));
        if (isSucceed == null || isSucceed == 0) {
            return new ApiResult<>(Code.SAVA_ERR,null, "修改失败");
        }
        return new ApiResult<>(Code.SAVA_OK,null, "修改失败");
    }

    @Override
    public ApiResult<List<User>> getAll() {
        List<User> userList = userDao.getAll();
        return new ApiResult<>(Code.SAVA_ERR,userList, String.valueOf(userList.size()));
    }

    @Override
    public ApiResult<Object> deleteUser(Integer uId) {
        Integer integer = userDao.deleteUser(uId);
        boolean isSuccess = integer != 0;
        return new ApiResult<>(isSuccess ? Code.DELETE_OK : Code.DELETE_ERR, null,
                isSuccess ? "已删除用户！" : "删除失败！");
    }
}
