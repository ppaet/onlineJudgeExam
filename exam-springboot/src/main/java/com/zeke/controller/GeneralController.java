package com.zeke.controller;


import com.zeke.service.GeneralService;
import com.zeke.bean.Difficulty;
import com.zeke.bean.Role;
import com.zeke.bean.Tag;
import com.zeke.bean.TopicType;
import com.zeke.utils.Code;
import com.zeke.utils.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GeneralController {

    @Autowired
    private GeneralService generalService;

    @GetMapping("/checkToken")
    public ApiResult<Object> checkToken() {
        return new ApiResult<>(Code.GET_OK, null, "token尚未过期！");
    }

    @GetMapping("/tag/{uId}")
    public ApiResult<List<Tag>> getTag(@PathVariable Integer uId) {
        return new ApiResult<>(Code.GET_OK, generalService.getTagList(uId), null);
    }

    @GetMapping("/topicType")
    public ApiResult<List<TopicType>> getTypeList() {
        return new ApiResult<>(Code.GET_OK, generalService.getTypeList(), null);
    }

    @GetMapping("/difficulty")
    public ApiResult<List<Difficulty>> getDifficultyList() {
        return new ApiResult<>(Code.GET_OK, generalService.getDifficultyList(), null);
    }

    @GetMapping("/role")
    public ApiResult<List<Role>> getRoleList() {
        return new ApiResult<>(Code.GET_OK, generalService.getRoleList(), null);
    }
}
