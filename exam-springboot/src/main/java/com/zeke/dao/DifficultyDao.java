package com.zeke.dao;


import com.zeke.bean.Difficulty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface DifficultyDao {

    @Select("select d_id, name from difficulty")
    ArrayList<Difficulty> selectAll();

    @Select("select name from difficulty where d_id=#{dId}")
    String selectById(Integer dId);
}
