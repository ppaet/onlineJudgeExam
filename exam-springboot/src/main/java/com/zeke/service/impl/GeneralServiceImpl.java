package com.zeke.service.impl;

import com.zeke.service.GeneralService;
import com.zeke.bean.Difficulty;
import com.zeke.bean.Role;
import com.zeke.bean.Tag;
import com.zeke.bean.TopicType;
import com.zeke.dao.DifficultyDao;
import com.zeke.dao.RoleDao;
import com.zeke.dao.TagDao;
import com.zeke.dao.TopicTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GeneralServiceImpl implements GeneralService {

    @Autowired
    private TagDao tagDao;
    @Autowired
    private TopicTypeDao topicTypeDao;
    @Autowired
    private DifficultyDao difficultyDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public ArrayList<Tag> getTagList(Integer uId) {
        return tagDao.selectAll(uId);
    }

    @Override
    public ArrayList<TopicType> getTypeList() {
        return topicTypeDao.selectAll();
    }

    @Override
    public ArrayList<Difficulty> getDifficultyList() {
        return difficultyDao.selectAll();
    }

    @Override
    public ArrayList<Role> getRoleList() {
        return roleDao.selectAll();
    }
}
