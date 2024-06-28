package com.zeke.service;

import com.zeke.bean.Difficulty;
import com.zeke.bean.Role;
import com.zeke.bean.Tag;
import com.zeke.bean.TopicType;

import java.util.ArrayList;

public interface GeneralService {

    ArrayList<Tag> getTagList(Integer uId);

    ArrayList<TopicType> getTypeList();

    ArrayList<Difficulty> getDifficultyList();

    ArrayList<Role> getRoleList();
}
