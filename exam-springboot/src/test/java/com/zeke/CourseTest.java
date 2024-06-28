package com.zeke;

import com.alibaba.fastjson.JSONObject;
import com.zeke.controller.CourseController;
import com.zeke.dao.CourseDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class CourseTest {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseController courseController;

    @Test
    void joinTest() {
        System.out.println(courseDao.isJoin(2, 8));
    }

    @Test
    void getStudentbyuid() {

        ArrayList<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(1);
        list.add(2);
        ArrayList<JSONObject> jsonObjects = courseDao.selectStudentAndCourse(9);
        System.out.println("jsonObjects SIZE = =========" + jsonObjects.size());
        for (JSONObject j :
                jsonObjects) {
            System.out.println(j);
        }
    }
}
