package com.zeke.service;

import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.Exam;
import com.zeke.bean.ExamAnswer;
import com.zeke.bean.StudentExam;
import com.zeke.utils.result.ApiResult;
import com.zeke.utils.result.TempResult;

import java.util.List;


public interface ExamService {

    // 创建考试
    TempResult createExam(Exam exam);

    // 注销考试
    TempResult deleteExam(Integer examId);

    // 信息更新 时间、试卷。
    ApiResult<Exam> updateExamInfo(Exam exam);

    // 查询
    ApiResult<List<Exam>> selectAll(Integer uId);

    ApiResult<Exam> selectOne(Integer examId);

    ApiResult<?> judge(JSONObject jsonObject);

    ApiResult<List<JSONObject>> getExamListByStu(Integer uId);

    ApiResult<Object> getExams();

    ApiResult<Boolean> isSubmit(Integer uId, Integer eId);

    ApiResult<List<StudentExam>> submitList(Integer uId);

    ApiResult<ExamAnswer> selectAnswer(Integer seId);

    ApiResult<List<JSONObject>> examReview(Integer uId);

    ApiResult<Object> reviseScore(Integer seId, Double score);

    ApiResult<Object> addComments(Integer seId, String comments);
}
