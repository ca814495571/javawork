package com.cqfc.samplemodule.dao;

import com.cqfc.samplemodule.dao.mapper.UserMapper;
import com.cqfc.samplemodule.protocol.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {


    @Autowired
    UserMapper userMapper;

    public List<User> getUserList()  {
        return userMapper.getUserList();
    }

    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

}
