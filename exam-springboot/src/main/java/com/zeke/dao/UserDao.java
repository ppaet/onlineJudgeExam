package com.zeke.dao;

import com.zeke.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

@Mapper
//@Repository
public interface UserDao {

    @Insert("insert into user values(null, #{rId}, " +
            "#{username}, #{password}, #{email}, 1, #{registerTime})")
    Integer addUser(User user);

    @Select("select * from user where email=#{email} and r_id=#{rId} and is_exist = 1")
    ArrayList<User> selectByEmail(User user);

    @Update("update user set password=#{password} where email=#{email} and r_id=#{rId}")
    Integer updateUser(User user);

    @Select("select username from user where u_id = #{uId}")
    String getNameById(Integer uId);

    @Select("<script>" +
            "select * from user where r_id=1 and is_exist = 1 and u_id in " +
            "<foreach item='uId' collection='uIds' open='(' separator=',' close=')'>" +
            "#{uId}" +
            "</foreach>" +
            "order by register_time desc" +
            "</script>")
    List<User> getByIds(List<Integer> uIds);

    @Select("select * from user where is_exist = 1")
    List<User> getAll();

    @Update("update user set is_exist=0 where u_id=#{uId}")
    Integer deleteUser(Integer uId);
}
