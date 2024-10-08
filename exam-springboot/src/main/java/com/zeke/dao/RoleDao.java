package com.zeke.dao;

import com.zeke.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface RoleDao {

    @Select("select r_id, name from role")
    ArrayList<Role> selectAll();
}
