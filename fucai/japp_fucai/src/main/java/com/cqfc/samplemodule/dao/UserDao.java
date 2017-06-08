package com.cqfc.samplemodule.dao;

import com.cqfc.samplemodule.protocol.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    //dummy implement.
    public List<User> getUserList()  {
        List<User> userList = new ArrayList<User>();
        userList.add(new User(1, "Bill Gates", 100, "Seattle"));
        userList.add(new User(2, "Larry Page", 200, "California"));
        return userList;
    }

}
