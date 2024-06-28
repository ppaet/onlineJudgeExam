package com.zeke.dao;

import com.zeke.bean.OnlineJudge;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CodeJudgeDao {

    @Insert("insert into program_test_case values(null,#{tId},#{testIn1},#{testOut1},#{testIn2},#{testOut2},#{testIn3},#{testOut3})")
    boolean addTestCase(OnlineJudge onlineJudge);

    @Select("select * from program_test_case where t_id = #{tId}")
    OnlineJudge getCase(Integer tId);
}
