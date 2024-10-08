package com.zeke.dao;

import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.Course;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface CourseDao {

    @Select("select * from course where u_id= #{uId} and is_exist=1 ORDER BY create_time DESC")
    ArrayList<Course> selectByUId(Integer uId);

    /**
     * 查询该老师所教课程的所有学生
     *
     * @param uId 老师Id
     * @return
     */
    @Select("select sc.u_id as uId, course.c_id as cId, sc.uc_id as ucId," +
            "user.userName, user.email, " +
            "course.name as courseName " +
            "FROM user,course,student_course as sc " +
            "WHERE user.u_id = sc.u_id  AND sc.c_id = course.c_id AND course.u_id = #{uId} ORDER BY create_time DESC")
    ArrayList<JSONObject> selectStudentAndCourse(Integer uId);

    @Update("update student_course set c_id=#{cId} where uc_id = #{ucId}")
    Integer updateStudentByCourse(Integer cId, Integer ucId);

    @Insert("insert into course values(null,#{uId},#{name},#{courseCode},#{createTime},1)")
    boolean addCourse(Course course);

    @Update("update course set is_exist=0 where c_id=#{cId}")
    boolean deleteCourse(Integer cId);

    @Update("update course set name=#{name} where c_id=#{cId}")
    boolean updateName(Course course);

    @Select("select c_id from course where is_exist=1 and course_code=#{code}")
    Integer selectByCode(String code);

    @Insert("insert into student_course values(null, #{uId}, #{cId})")
    boolean joinCourse(Integer uId, Integer cId);

    @Select("select count(1) from student_course where u_id=#{uId} and c_id=#{cId}")
    Integer isJoin(Integer uId, Integer cId);

    @Select("select * from student_course where u_id=#{uId} and c_id=#{cId}")
    ArrayList<JSONObject> seltectJoin(Integer uId, Integer cId);

    @Update("delete from student_course where uc_id=#{ucId}")
    Integer deleteStudentByCourse(Integer ucId);

    /**
     * 用在这个函数关联com.zeke.dao.ExamDao#selectOne(java.lang.Integer)
     * @param cId
     * @return
     */
    @Select("select name as courseName from course where c_id=#{cId}")
    String selectNameByCid(Integer cId);

    /**
     * 学生查询已经加入的课程
     *
     * @param uId 学生Id
     * @return [{"teacherName": xxx, "courseName": xxx}]
     */
    @Select("select s2.c_id cId, s3.userName teacherName, s2.name courseName " +
            "from student_course s1, course s2, user s3 " +
            "where s1.u_id=${uId} and s1.c_id=s2.c_id and s2.u_id=s3.u_id;")
    ArrayList<JSONObject> selectChoiceCourseByUId(Integer uId);

    @Select({
            "<script>",
            "select * from course where u_id = #{uId} and is_exist = 1",
            "<if test='name != null and name != \"\"'>",
            "and name like concat('%', #{name}, '%')",
            "</if>",
            "<if test='courseCode != null and courseCode != \"\"'>",
            "and course_code = #{courseCode}",
            "</if>",
            "ORDER BY create_time DESC",
            "</script>"
    })
    ArrayList<Course> select(@Param("uId") Integer uId, @Param("name") String name, @Param("courseCode") String courseCode);
}
