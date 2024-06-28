package com.zeke.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.*;
import com.zeke.bean.vo.StudentExamVO;
import com.zeke.bean.vo.TestJudgeVO;
import com.zeke.bean.vo.TopicAnswerVO;
import com.zeke.dao.CodeJudgeDao;
import com.zeke.dao.CourseDao;
import com.zeke.dao.ExamDao;
import com.zeke.dao.UserDao;
import com.zeke.service.CodeJudgeService;
import com.zeke.service.ExamService;
import com.zeke.utils.Code;
import com.zeke.utils.ExamUtils;
import com.zeke.utils.result.ApiResult;
import com.zeke.utils.result.TempResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamDao examDao;

    @Autowired
    private CodeJudgeDao codeJudgeDao;

    @Autowired
    private CodeJudgeService codeJudgeService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CourseDao courseDao;

    //创建考试
    @Override
    public TempResult createExam(Exam exam) {
        System.out.println("create Exam print" + exam.toString());
        TempResult tempResult = new TempResult();
        Integer exam1 = examDao.createExam(exam);
        if (exam1 != 0) {
            tempResult.setFlag(true);
            tempResult.setMsg("创建成功！");
        } else {
            tempResult.setFlag(false);
            tempResult.setMsg("创建失败！");
        }
        return tempResult;
    }

    //注销考试
    @Override
    public TempResult deleteExam(Integer examId) {
        TempResult tempResult = new TempResult();
        Integer exam1 = examDao.deleteExam(examId);
        if (exam1 != 0) {
            tempResult.setFlag(true);
            tempResult.setMsg("删除成功！");
        } else {
            tempResult.setFlag(false);
            tempResult.setMsg("删除失败！");
        }
        return tempResult;
    }


    //考试信息更改 时间、试卷等。
    @Override
    public ApiResult<Exam> updateExamInfo(Exam exam) {
        Integer flag = examDao.updateExamInfo(exam);//返回更新后的考试实体
        ApiResult<Exam> apiResult = new ApiResult<>();

        if (flag != null) {
            apiResult.setMsg("修改成功");
            apiResult.setData(exam);
        } else {
            apiResult.setMsg("修改失败");
            apiResult.setData(null);//返回原exam
        }
        return apiResult;
    }

    @Override
    public ApiResult<List<Exam>> selectAll(Integer uId) {
        ApiResult<List<Exam>> apiResult = new ApiResult<>();
//        Page<?> page = PageHelper.startPage(Integer.parseInt(pageNumNow), 5);  //设置第几条记录开始，多少条记录为一页
        //通过userService获取user的信息，其sql语句为"select * from user" 但因pageHelper已经注册为插件，所以pageHelper会在原sql语句上增加limit，从而实现分页
        List<Exam> exams = examDao.selectAll(uId);//因而获得的是分好页的结果集
//        PageInfo<?> pageHelper = page.toPageInfo(); //获取页面信息的对象，里面封装了许多页面的信息 如：总条数，当前页码，需显示的导航页等等
        apiResult.setData(exams);
        if (apiResult.getData() != null) {
            apiResult.setMsg("数据查询成功");
            apiResult.setCode(Code.GET_OK);
        } else {
            apiResult.setMsg("数据查询失败!");
            apiResult.setCode(Code.GET_ERR);
        }
        return apiResult;
    }

    @Override
    public ApiResult<Exam> selectOne(Integer examId) {
        /**
         * 查询结果
         */
        Exam exam = examDao.selectOne(examId);
        ApiResult<Exam> apiResult = new ApiResult<>();
        if (exam != null) {
            apiResult.setData(exam);
            apiResult.setCode(Code.GET_OK);
            apiResult.setMsg("查询成功！");
        } else {
            apiResult.setData(null);
            apiResult.setCode(Code.GET_ERR);
            apiResult.setMsg("查询失败！");
        }
        return apiResult;
    }

    /**
     * 学生获取自己的所有考试信息
     *
     * @param uId 学生的uid
     * @return
     */
    public ApiResult<List<JSONObject>> getExamListByStu(Integer uId) {
        List<Course> coursesByUid = examDao.getCoursesByUid(uId);
        ArrayList<Integer> cIds = new ArrayList<>();
        for (Course course : coursesByUid) {
            cIds.add(course.getcId());
        }
        ArrayList<JSONObject> res = examDao.getExamsByCourseId(cIds);
        return new ApiResult<>(Code.GET_OK, res, "查询成功");
    }

    /**
     * 对待评分答试卷和参考答案试卷进行题型提取，使用判分util进行评分
     *
     * @param jsonObject 前端传来的json数据
     * @return apiresult中是一个含有分数、正确答案的新的paper
     */
    @Override
    public ApiResult<?> judge(JSONObject jsonObject) {
        Integer uId = jsonObject.getInteger("uId");
        Integer eId = jsonObject.getInteger("eId");
        ApiResult<Boolean> isSub = isSubmit(uId, eId);
        if (!isSub.getData()) return isSub;
        //  获取正确答案
        Exam exam = examDao.selectOne(eId);
        JSONObject rightContent = JSON.parseObject(exam.getContent());
        //  学生提交的答案
        JSONObject content = jsonObject.getJSONObject("content");
        ExamAnswer examAnswer = new ExamAnswer();
        List<TopicAnswerVO> studentAnswerList = new ArrayList<>();
        ArrayList<JSONObject> res = new ArrayList<>();
        AtomicReference<Double> totalScore = new AtomicReference<>(0.0);
        content.keySet().forEach((key) -> {
            JSONArray topicList = content.getJSONArray(key);
            JSONArray rightTopicList = rightContent.getJSONArray(key);
            for (int i = 0; i < topicList.size(); i++) {
                TopicAnswerVO topicAnswerVO = new TopicAnswerVO();
                JSONObject topic = topicList.getJSONObject(i);
                JSONObject rightTopic = rightTopicList.getJSONObject(i);
                String studentAnswer = topic.getJSONObject("answer").getString("answerContent");
                String rightAnswer = rightTopic.getJSONObject("answer").getString("answerContent");
                topic.put("rightAnswer", rightAnswer);
                // 编程题判分
                if (Integer.parseInt(key) == 5) {
                    Integer tId = rightTopic.getInteger("tId");
                    OnlineJudge onlineJudge = codeJudgeDao.getCase(tId);
                    List<TestJudgeVO> testJudgeVOList = new ArrayList<>();
                    TestJudgeVO testJudgeVO1 = new TestJudgeVO();
                    TestJudgeVO testJudgeVO2 = new TestJudgeVO();
                    TestJudgeVO testJudgeVO3 = new TestJudgeVO();
                    testJudgeVO1.setTestOut(onlineJudge.getTestOut1());
                    testJudgeVO1.setTestIn(onlineJudge.getTestIn1());
                    testJudgeVO2.setTestOut(onlineJudge.getTestOut2());
                    testJudgeVO2.setTestIn(onlineJudge.getTestIn2());
                    testJudgeVO3.setTestOut(onlineJudge.getTestOut3());
                    testJudgeVO3.setTestIn(onlineJudge.getTestIn3());
                    testJudgeVOList.add(testJudgeVO1);
                    testJudgeVOList.add(testJudgeVO2);
                    testJudgeVOList.add(testJudgeVO3);
                    Double a = codeJudgeService.batchTest(studentAnswer, testJudgeVOList);
                    topic.put("getScore", topic.getDoubleValue("score")*a);
                    //多选题判分
                } else if (Integer.parseInt(key) == 3) {
                    JSONArray rightChoiceList = JSON.parseArray(rightAnswer);
                    JSONArray studentChoiceList = JSON.parseArray(studentAnswer);
                    boolean flag = true;
                    if (studentChoiceList.size() == 0) {
                        topic.put("getScore", 0);
                        return;
                    }
                    for (Object choice : studentChoiceList) {
                        boolean isRight = rightChoiceList.contains(choice);
                        if (!isRight) {
                            flag = false;
                            topic.put("getScore", 0);
                        }
                    }
                    if (flag && studentChoiceList.size() == rightChoiceList.size()) {
                        topic.put("getScore", topic.getDoubleValue("score"));
                    } else {
                        topic.put("getScore", topic.getDoubleValue("score") / 2);
                    }
                    // 其他类型的题目只要匹配字符串是否完全相等
                } else {
                    topic.put("getScore", rightAnswer.equals(studentAnswer) ? topic.getDoubleValue("score") : 0);
                }
                topicAnswerVO.settId(topic.getInteger("tId"));
                topicAnswerVO.setTypeId(topic.getJSONObject("answer").getInteger("typeId"));
                topicAnswerVO.setScore(topic.getDoubleValue("getScore"));
                topicAnswerVO.setStatus(topicAnswerVO.getScore() == topic.getDoubleValue("score") ? 0 : 1);
                topicAnswerVO.setQuestion(topic.getString("question"));
                topicAnswerVO.setAnswer(studentAnswer);
                topicAnswerVO.setRightAnswer(rightAnswer);
                studentAnswerList.add(topicAnswerVO);
                totalScore.updateAndGet(v -> v + topic.getDoubleValue("getScore"));
                res.add(topic);
            }
        });
        JSONObject result = new JSONObject();
        result.put("totalScore", totalScore);
        result.put("content", res);
        StudentExam studentExam = new StudentExam(uId, eId, result.toString(), totalScore.get());
        studentExam.setCreateTime(LocalDateTime.now());
        if (examDao.selectIsExist(studentExam) != 0) {
            return new ApiResult<>(Code.SAVA_ERR, null, "不可反复提交试卷！");
        }
        Integer integer = examDao.addExamRecord(studentExam);
        boolean isSuccess = integer != 0;
        if (isSuccess) {
            Integer seId = examDao.selectSeId(uId, eId);
            examDao.addExamAnswer(seId, JSON.toJSONString(studentAnswerList));
        }
        return new ApiResult<>(isSuccess ? Code.SAVA_OK : Code.SAVA_ERR, result,
                isSuccess ? "数据处理成功！" : "数据处理失败！");
    }

    public ApiResult<Object> getExams() {
        List<Exam> examList = examDao.findAll(); // 获取所有考试记录
        int count = 0;
        for (Exam exam : examList) { // 遍历考试记录
            Integer status = ExamUtils.getExamStatus(exam.getStartTime(), exam.getEndTime()); // 计算考试状态
            if (!Objects.equals(exam.getStatus(), status)) {
                exam.setStatus(status); // 将计算结果保存到考试记录中
                examDao.updateStatus(exam);
                count++;
            }
        }
        return new ApiResult<>(Code.UPDATE_OK, null, "考试状态更新成功，更新了" + count + "条数据"); // 返回所有考试记录
    }

    @Override
    public ApiResult<Boolean> isSubmit(Integer uId, Integer eId) {
        Integer count = examDao.isSubmit(uId, eId);
        boolean isSub = count == 0;
        return new ApiResult<>(Code.GET_OK, isSub,
                isSub ? "可以提交试卷！" : "已经提交试卷，不可重复提交！");
    }

    @Override
    public ApiResult<List<StudentExam>> submitList(Integer uId) {
        ArrayList<StudentExam> res = examDao.submitList(uId);
        return new ApiResult<>(Code.GET_OK, res,
                res.size() != 0 ? "查询到" + res.size() + "条数据！" : "查询结果为空！");
    }

    @Override
    public ApiResult<ExamAnswer> selectAnswer(Integer seId) {
        /**
         * 查询结果
         */
        JSONObject object = examDao.selectAnswer(seId);
        String jsonString = object.get("content").toString();
        JSONArray jsonArray = JSON.parseArray(jsonString);
        ExamAnswer examAnswer = new ExamAnswer();
        examAnswer.setAqId((Integer) object.get("aq_id"));
        examAnswer.setSeId((Integer) object.get("se_id"));
        examAnswer.setContent(jsonArray.toJavaList(TopicAnswerVO.class));
        ApiResult<ExamAnswer> apiResult = new ApiResult<>();
        if (examAnswer != null) {
            apiResult.setData(examAnswer);
            apiResult.setCode(Code.GET_OK);
            apiResult.setMsg("查询成功！");
        } else {
            apiResult.setData(null);
            apiResult.setCode(Code.GET_ERR);
            apiResult.setMsg("查询失败！");
        }
        return apiResult;
    }

    @Override
    public ApiResult<List<JSONObject>> examReview(Integer uId) {
        List<Exam> examList = examDao.selectAll(uId);
        List<StudentExam> res = examDao.getStudentExamByEIds(examList.stream().map(Exam::geteId).collect(Collectors.toList()));
        if (res.isEmpty()) {
            return new ApiResult<>(Code.GET_ERR, null, "查询结果为空！");
        }
        List<StudentExamVO> studentExamList = res.stream().map(StudentExamVO::new).collect(Collectors.toList());
        Map<Integer, String> examIdToNameMap = examList.stream()
                .collect(Collectors.toMap(Exam::geteId, Exam::getName));
        Map<Integer, Integer> examIdTocIdMap = examList.stream()
                .collect(Collectors.toMap(Exam::geteId, Exam::getcId));
        List<Course> courseList = courseDao.selectByUId(uId);
        Map<Integer, String> courseIdToNameMap = courseList.stream()
                .collect(Collectors.toMap(Course::getcId, Course::getName));
        List<User> userList = userDao.getByIds(studentExamList.stream().map(StudentExam::getuId).collect(Collectors.toList()));
        Map<Integer, String> usrIdToNameMap = userList.stream()
                .collect(Collectors.toMap(User::getuId, User::getUsername));
        for (StudentExamVO studentExamVO :studentExamList) {
            studentExamVO.setName(examIdToNameMap.get(studentExamVO.geteId()));
            studentExamVO.setCourseName(courseIdToNameMap.get(examIdTocIdMap.get(studentExamVO.geteId())));
            studentExamVO.setStudentName(usrIdToNameMap.get(studentExamVO.getuId()));
        }
        return new ApiResult<>(Code.GET_OK, convertToJSONList(studentExamList),
                studentExamList.size() != 0 ? "查询到" + studentExamList.size() + "条数据！" : "查询结果为空！");
    }

    @Override
    public ApiResult<Object> reviseScore(Integer seId, Double score) {
        Integer count = examDao.reviseScore(seId, score);
        boolean isSub = count == 0;
        return new ApiResult<>(Code.GET_OK, null,
                isSub ? "成绩修改成功！" : "成绩修改失败！");
    }

    @Override
    public ApiResult<Object> addComments(Integer seId, String comments) {
        Integer count = examDao.addComments(seId, comments);
        boolean isSub = count == 0;
        return new ApiResult<>(Code.GET_OK, null,
                isSub ? "添加批注成功！" : "添加批注失败！");
    }

    public <T> List<JSONObject> convertToJSONList(List<T> entityList) {
        return entityList.stream()
                .map(t -> (JSONObject)JSONObject.toJSON(t))
                .collect(Collectors.toList());
    }
}
