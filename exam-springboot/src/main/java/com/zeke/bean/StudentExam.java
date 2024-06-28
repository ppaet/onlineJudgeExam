package com.zeke.bean;

import java.time.LocalDateTime;

public class StudentExam {
    private Integer seId;
    private Integer uId;
    private Integer eId;
    private String rightStudentAnswer;

    private Double score;

    private String review;

    private LocalDateTime createTime;

    public StudentExam() {
    }

    public StudentExam(Integer uId, Integer eId, String rightStudentAnswer, Double score) {
        this.uId = uId;
        this.eId = eId;
        this.rightStudentAnswer = rightStudentAnswer;
        this.score = score;
        this.review = "";
    }

    public StudentExam(Integer seId, Integer uId, Integer eId, String rightStudentAnswer, Double score, String review, LocalDateTime createTime) {
        this.seId = seId;
        this.uId = uId;
        this.eId = eId;
        this.rightStudentAnswer = rightStudentAnswer;
        this.score = score;
        this.review = review;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "StudentExam{" +
                "seId=" + seId +
                ", uId=" + uId +
                ", eId=" + eId +
                ", rightStudentAnswer='" + rightStudentAnswer + '\'' +
                ", score=" + score +
                ", review='" + review + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getSeId() {
        return seId;
    }

    public void setSeId(Integer seId) {
        this.seId = seId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer geteId() {
        return eId;
    }

    public void seteId(Integer eId) {
        this.eId = eId;
    }

    public String getRightStudentAnswer() {
        return rightStudentAnswer;
    }

    public void setRightStudentAnswer(String rightStudentAnswer) {
        this.rightStudentAnswer = rightStudentAnswer;
    }
}
