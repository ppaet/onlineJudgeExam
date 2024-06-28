package com.zeke.service;

import com.zeke.bean.OnlineJudge;
import com.zeke.bean.TopicTest;
import com.zeke.bean.vo.TestJudgeVO;

import java.util.List;

public interface CodeJudgeService {

    Boolean doJudge(Integer language, String code, String testCase, String answer);

    String testJudge(TestJudgeVO testJudgeVO);

    String createTopic(OnlineJudge onlineJudge);

    String topicTest(TopicTest topicTest);

    Double batchTest(String code, List<TestJudgeVO> testJudgeVOList);
}
