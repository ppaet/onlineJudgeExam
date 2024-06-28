package com.zeke.bean;

import com.zeke.bean.vo.TopicAnswerVO;

import java.util.List;

public class ExamAnswer {

    private Integer aqId;

    private Integer seId;

    private List<TopicAnswerVO> content;

    public Integer getAqId() {
        return aqId;
    }

    public void setAqId(Integer aqId) {
        this.aqId = aqId;
    }

    public Integer getSeId() {
        return seId;
    }

    public void setSeId(Integer seId) {
        this.seId = seId;
    }

    public List<TopicAnswerVO> getContent() {
        return content;
    }

    public void setContent(List<TopicAnswerVO> content) {
        this.content = content;
    }
}
