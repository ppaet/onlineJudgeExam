package com.zeke.dao;


import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.Tag;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface TagDao {

    @Select("select tag_id, u_id, name from tag where u_id in (0,#{uId})")
    ArrayList<Tag> selectAll(Integer uId);

    @Select("select name from tag where tag_id=#{tagId}")
    String selectById(Integer tagId);


    @Insert("insert into tag values(null,#{uId},#{name})")
    Integer addTag(JSONObject tag);

    @Update("UPDATE tag SET u_id = #{uId}, name = #{name} where tag_id =#{tagId};")
    Integer updateTag(JSONObject tag);

    @Delete("delete from tag where tag_id = #{tagId}")
    Integer deleteTag(Integer tagId);
}
