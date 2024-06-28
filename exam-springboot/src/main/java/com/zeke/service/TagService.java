package com.zeke.service;

import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.Tag;
import com.zeke.utils.result.ApiResult;

import java.util.List;

public interface TagService {
    ApiResult<Integer> updateTag(JSONObject tag);

    ApiResult<List<Tag>> selectAll(Integer uId);

    ApiResult<Integer> addTag(JSONObject json);

    ApiResult<Integer> deleteTag(Integer tagId);

}
