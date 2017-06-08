package com.cqfc.samplemodule.service.impl;

import com.cqfc.samplemodule.dao.UserDao;
import com.cqfc.samplemodule.protocol.SampleService;
import com.cqfc.samplemodule.protocol.User;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("sampleService")
public class SampleServiceImpl implements SampleService.Iface {

    @Resource
    UserDao userDao;

    @Override
    public List<User> getUserList() throws TException {
        return userDao.getUserList();
    }
}
