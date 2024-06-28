package com.zeke.bean.vo;

import com.zeke.bean.StudentExam;

public class StudentExamVO extends StudentExam {
    private String name;

    private String courseName;

    private String studentName;

    public StudentExamVO(StudentExam studentExam) {
        this.setSeId(studentExam.getSeId());
        this.setuId(studentExam.getuId());
        this.seteId(studentExam.geteId());
        this.setRightStudentAnswer(studentExam.getRightStudentAnswer());
        this.setScore(studentExam.getScore());
        this.setReview(studentExam.getReview());
        this.setCreateTime(studentExam.getCreateTime());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
