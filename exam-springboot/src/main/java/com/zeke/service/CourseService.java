package com.zeke.service;

import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.Course;
import com.zeke.utils.result.ApiResult;
import com.zeke.utils.result.TempResult;

import java.util.ArrayList;
import java.util.List;

public interface CourseService {

    ApiResult<List<Course>> selectByUId(Integer uId);

    boolean addCourse(Course course);

    boolean deleteCourse(Integer cId);

    boolean updateName(Course course);

    TempResult joinCourse(Integer uId, String code);

    /**
     *
     * @param uId 教师的uId
     * @return ApiResult接口
     */
    ApiResult<List<JSONObject>> selectStudentAndCourse(Integer uId);

    /**
     * 根据学生Id或者学生姓名搜索
     * @param uId 教师Id
     * @param studentIdOrNameOrCourseName 学生Id或者姓名或者课程名称
     * @return ApiResult接口
     */
    ApiResult<List<JSONObject>> searchStudentAndCourse(Integer uId, String studentIdOrNameOrCourseName);

    /**
     * 根据课程名进行搜索
     * @param uId 教师Id
     * @param courseName 课程名
     * @return ApiResult接口
     */
    ApiResult<List<Course>> searchCourseByName(Integer uId, String courseName);

    /**
     * 修改学生所选的课程
     * @param cId 课程Id
     * @param ucId 选课Id
     * @return ApiResult接口
     */
    ApiResult<Object> updateStudentByCourse(Integer cId, Integer ucId);

    /**
     * 将学生移除课程
     * @param ucId 选课Id
     * @return ApiResult接口
     */
    ApiResult<Object> deleteStudentByCourse(Integer ucId);

    ApiResult<ArrayList<JSONObject>> selectChoiceCourseByUId(Integer uId);

    ApiResult<List<Course>> select(Integer uId, String name, String courseCode);

    /**
     * 学生退出课程
     * @param uId 学生Id
     * @param cId 课程Id
     * @return
     */
    ApiResult<Object> studentQuitCourse(Integer uId, Integer cId);
}
