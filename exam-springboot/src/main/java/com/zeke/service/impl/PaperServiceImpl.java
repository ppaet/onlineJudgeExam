package com.zeke.service.impl;

import com.zeke.service.PapersService;
import com.zeke.bean.Papers;
import com.zeke.dao.PapersDao;
import com.zeke.utils.Code;
import com.zeke.utils.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaperServiceImpl implements PapersService {

    @Autowired
    private PapersDao papersDao;

    @Override
    public ApiResult<List<Papers>> selectAllByUId(Integer uId) {
        ArrayList<Papers> papersList = papersDao.selectAllByUId(uId);
        int count = papersList.size();
        return new ApiResult<>(Code.GET_OK, papersList,
                count != 0 ? "所用试卷共" + count + "套试卷！" : "暂无试卷，请添加！");
    }

    @Override
    public ApiResult<Object> deleteByPId(Integer pId) {
        Integer integer = papersDao.deleteByPId(pId);
        boolean isSuccess = integer != 0;
        return new ApiResult<>(isSuccess ? Code.DELETE_OK : Code.DELETE_ERR, null,
                isSuccess ? "删除试卷成功！" : "删除试卷失败！");
    }

    @Override
    public ApiResult<Object> updateName(Integer pId, String name) {
        Integer integer = papersDao.updateName(pId, name);
        boolean isSuccess = integer != 0;
        return new ApiResult<>(isSuccess ? Code.UPDATE_OK : Code.UPDATE_ERR, null,
                isSuccess ? "试卷名称修改成功！" : "试卷名称修改失败！");
    }

    @Override
    public ApiResult<List<Papers>> searchByName(Integer uId, String name) {
        ArrayList<Papers> papersList = papersDao.selectAllByUId(uId);
        List<Papers> res = papersList.stream()
                .filter(item -> name.equals(item.getName()))
                .collect(Collectors.toList());
        int count = res.size();
        return new ApiResult<>(Code.GET_OK, res,
                count != 0 ? "查询出" + count + "套试卷！" : "未找到结果！");
    }
}
