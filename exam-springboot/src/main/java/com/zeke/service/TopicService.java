package com.zeke.service;

import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.Topic;
import com.zeke.utils.result.ApiResult;
import com.zeke.utils.result.TempResult;

import java.util.ArrayList;
import java.util.List;

public interface TopicService {

    TempResult addTopic(Topic topic);

    ArrayList<Topic> selectByUId(Integer uId);

    TempResult delTopic(Integer tId);

    TempResult updateTopic(Topic topic);

    TempResult topicToPapers(Integer uId, Integer[] tIds, String papersName, JSONObject topicScore);

    TempResult topicToPapers(JSONObject params);

    ApiResult<List<Topic>> searchByInfo(Integer uId, String info);
}
