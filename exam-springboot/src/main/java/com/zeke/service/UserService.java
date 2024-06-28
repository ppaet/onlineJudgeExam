package com.zeke.service;

import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.User;
import com.zeke.utils.result.ApiResult;
import com.zeke.utils.result.TempResult;

import java.util.List;

public interface UserService {
    TempResult register(User user);

    ApiResult<JSONObject> login(User user);

    TempResult updatePassword(User user);

    ApiResult<Object> logout(String uId);

    ApiResult<Object> forgetpw(User user);

    ApiResult<List<User>> getAll();

    ApiResult<Object> deleteUser(Integer uId);
}
