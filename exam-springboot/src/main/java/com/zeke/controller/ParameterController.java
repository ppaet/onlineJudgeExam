package com.zeke.controller;

import com.zeke.bean.OnlineJudge;
import com.zeke.bean.TopicTest;
import com.zeke.bean.vo.TestJudgeVO;
import com.zeke.service.CodeJudgeService;
import com.zeke.utils.Code;
import com.zeke.utils.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oj")
public class ParameterController {
    @Autowired
    private CodeJudgeService codeJudgeService;

    @PostMapping("/topicTest")
    public ApiResult<Object> topicTest(@RequestBody TopicTest topicTest) {
        return new ApiResult<>(Code.SAVA_OK, null, codeJudgeService.topicTest(topicTest));
    }

    @PostMapping("/testCase")
    public ApiResult<Object> testJudge(@RequestBody TestJudgeVO testJudgeVO) {
        return new ApiResult<>(Code.SAVA_OK, null, codeJudgeService.testJudge(testJudgeVO));
    }

    @PostMapping("")
    public ApiResult<Object> createTopic(@RequestBody OnlineJudge onlineJudge) {
        return new ApiResult<>(Code.SAVA_OK, null, codeJudgeService.createTopic(onlineJudge));
    }
}
