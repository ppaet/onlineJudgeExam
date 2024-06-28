package com.zeke.service;

import com.zeke.bean.Papers;
import com.zeke.utils.result.ApiResult;

import java.util.List;

public interface PapersService {

    ApiResult<List<Papers>> selectAllByUId(Integer uId);

    ApiResult<Object> deleteByPId(Integer pId);

    ApiResult<Object> updateName(Integer pId, String name);

    ApiResult<List<Papers>> searchByName(Integer uId, String name);
}
