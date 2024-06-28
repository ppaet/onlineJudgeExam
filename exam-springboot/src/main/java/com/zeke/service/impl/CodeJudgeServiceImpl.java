package com.zeke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.Cmd;
import com.zeke.bean.OnlineJudge;
import com.zeke.bean.Topic;
import com.zeke.bean.TopicTest;
import com.zeke.bean.vo.TestJudgeVO;
import com.zeke.dao.CodeJudgeDao;
import com.zeke.enumeration.EnumJudgeApi;
import com.zeke.service.CodeJudgeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * oj判题系统
 */
@Service
public class CodeJudgeServiceImpl implements CodeJudgeService {

    @Value("${judge.host}")
    private String JUDGE_HOST;

    @Autowired
    private CodeJudgeDao codeJudgeDao;

    @Autowired
    private TopicServiceImpl topicService;


    @Override
    public String topicTest(TopicTest topicTest) {
        OnlineJudge onlineJudge = codeJudgeDao.getCase(topicTest.gettId());
        String fileId = this.setJudge(topicTest.getCode());
        if (fileId.equals("ERROR")) {
            return "ERROR";
        }
        if (onlineJudge.getTestOut1().equals(this.judge(fileId, onlineJudge.getTestIn1()))&&
                onlineJudge.getTestOut2().equals(this.judge(fileId, onlineJudge.getTestIn2()))&&
                onlineJudge.getTestOut3().equals(this.judge(fileId, onlineJudge.getTestIn3()))) {
            return "SUCCESS";
        }
        return "ERROR";
    }

    /**
     * 批量判题
     * @param code 代码
     * @param testJudgeVOList 测试用例列表
     * @return 返回正确比例
     */
    @Override
    public Double batchTest(String code, List<TestJudgeVO> testJudgeVOList) {
        String fileId = this.setJudge(code);
        if (fileId.equals("ERROR")) {
            return 0.0;
        }
        int ok = 0;
        for (TestJudgeVO testJudgeVO : testJudgeVOList) {
            Boolean ret = testJudgeVO.getTestOut().equals(this.judge(fileId, testJudgeVO.getTestIn()));
            if (ret) {
                ok++;
            }
        }
        this.delete(fileId);
        return (double) (testJudgeVOList.size()/ok);
    }

    /**
     * 判题
     * @param language 编程语言
     * @param code 代码
     * @param testCase 测试用例
     * @param answer 正确答案
     * @return 正确与否
     */
    @Override
    public Boolean doJudge(Integer language, String code, String testCase, String answer) {
        String fileId = this.setJudge(code);
        if (fileId.equals("ERROR")) {
            return false;
        }
        Boolean ret = answer.equals(this.judge(fileId, testCase));
        this.delete(fileId);
        return ret;
    }

    @Override
    public String testJudge(TestJudgeVO testJudgeVO) {
        String fileId = this.setJudge(testJudgeVO.getAnswer());
        if (fileId.equals("ERROR")) {
            return "CODE ERROR";
        }
        String ret = this.judge(fileId, testJudgeVO.getTestIn());
        this.delete(fileId);
        if (ret.equals(testJudgeVO.getTestOut())) {
            return "SUCCESS";
        } else {
            return ret;
        }
    }

    @Override
    @Transactional
    public String createTopic(OnlineJudge onlineJudge) {
        Topic topic = new Topic();
        BeanUtils.copyProperties(onlineJudge, topic);
        topic.setCreateTime(LocalDateTime.now());
        topic.setTypeId(5);
        topicService.addTopic(topic);
        onlineJudge.settId(topicService.selectByUId(topic.getuId()).get(0).gettId());
        codeJudgeDao.addTestCase(onlineJudge);
        return "SUCCESS";
    }

    /**
     * 代码写入判题机
     * @param code 作答代码
     * @return 返回文件ID
     */
    private String setJudge(String code) {
        Cmd cmd = packageCmd(code);
        List<Cmd> cmdList = new ArrayList<>();
        cmdList.add(cmd);
        Map<String, List<Cmd>> cmdMap = new HashMap<>();
        cmdMap.put("Cmd", cmdList);
        JSONArray jsonArray = postClient(JUDGE_HOST+ EnumJudgeApi.RUN.getApi(), cmdMap);
        if (jsonArray != null) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String fileId;
            try {
                fileId = jsonObject.getJSONObject("fileIds").get("Main.class").toString();
            } catch (Exception e) {
                return "ERROR";
            }
            return fileId;
        } else {
            throw new IllegalAccessError();
        }
    }

    /**
     * 判题
     * @param fileId 文件ID
     * @param testCase 测试用例
     * @return 判断该用例是否正确
     */
    private String judge(String fileId, String testCase) {
        Cmd cmd = compileCmd(fileId, testCase);
        List<Cmd> cmdList = new ArrayList<>();
        cmdList.add(cmd);
        Map<String, List<Cmd>> cmdMap = new HashMap<>();
        cmdMap.put("Cmd", cmdList);
        JSONArray jsonArray = postClient(JUDGE_HOST+ EnumJudgeApi.RUN.getApi(), cmdMap);
        if (jsonArray != null) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String stdout = jsonObject.getJSONObject("files").get("stdout").toString();
            return stdout;
        } else {
            throw new IllegalAccessError();
        }
    }

    /**
     * 删除文件防止内存溢出
     * @param fileId 文件ID
     */
    private void delete(String fileId) {
        // 创建RestTemplate对象
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 发送DELETE请求
        String url = JUDGE_HOST + "/file/" + fileId;
        ResponseEntity<JSONArray> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE, new HttpEntity<>(headers), JSONArray.class);
    }

    /**
     * 打包请求体
     * @param code
     * @return
     */
    private Cmd packageCmd(String code) {
        Cmd cmd = new Cmd();
        cmd.setArgs(Arrays.asList("/usr/lib/jvm/jdk1.8.0_401/bin/javac", "Main.java"));
        cmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        List<Object> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(new JSONObject().fluentPut("content", ""));
        JSONObject stdoutObject = new JSONObject();
        stdoutObject.put("name", "stdout");
        stdoutObject.put("max", 10240);
        jsonObjectList.add(stdoutObject);
        JSONObject stderrObject = new JSONObject();
        stderrObject.put("name", "stderr");
        stderrObject.put("max", 10240);
        jsonObjectList.add(stderrObject);
        cmd.setFiles(jsonObjectList);
        cmd.setCpuLimit(10000000000L);
        cmd.setMemoryLimit(104857600L);
        cmd.setProcLimit(50);
        JSONObject contnetJson = new JSONObject();
        contnetJson.put("content", code);
        JSONObject mainJson = new JSONObject();
        mainJson.put("Main.java", contnetJson);
        cmd.setCopyIn(mainJson);
        cmd.setCopyOutCached(Arrays.asList("Main.class"));
        return cmd;
    }

    /**
     * 编译运行请求体
     * @param fileId 文件ID
     * @param parameter 参数
     * @return
     */
    private Cmd compileCmd(String fileId, String parameter) {
        Cmd cmd = new Cmd();
        cmd.setArgs(Arrays.asList("/usr/lib/jvm/jdk1.8.0_401/bin/java", "Main"));
        cmd.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        List<Object> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(new JSONObject().fluentPut("content", parameter));
        JSONObject stdoutObject = new JSONObject();
        stdoutObject.put("name", "stdout");
        stdoutObject.put("max", 10240);
        jsonObjectList.add(stdoutObject);
        JSONObject stderrObject = new JSONObject();
        stderrObject.put("name", "stderr");
        stderrObject.put("max", 10240);
        jsonObjectList.add(stderrObject);
        cmd.setFiles(jsonObjectList);
        cmd.setCpuLimit(10000000000L);
        cmd.setMemoryLimit(104857600L);
        cmd.setProcLimit(50);
        JSONObject contnetJson = new JSONObject();
        contnetJson.put("fileId", fileId);
        JSONObject mainJson = new JSONObject();
        mainJson.put("Main.class", contnetJson);
        cmd.setCopyIn(mainJson);
        return cmd;
    }

    private JSONArray postClient(String url, Map<String, List<Cmd>> parameter) {
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 创建HttpEntity对象，包含请求体和请求头
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(parameter), headers);
        ResponseEntity<JSONArray> responseEntity = restTemplate.postForEntity(url, requestEntity, JSONArray.class);
        return responseEntity.getBody();
    }

}
