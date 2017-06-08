package com.cqfc.samplemodule.service.impl;

import com.cqfc.samplemodule.dao.UserDao;
import com.cqfc.samplemodule.protocol.SampleService;
import com.cqfc.samplemodule.protocol.User;
import com.jami.common.dao.DataSourceEntry;
import com.jami.common.dao.DynamicDataSourceContextHolder;
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

    @Override
    public User getUserById(int id) throws TException {
        if (id % 2 == 0) {
            DynamicDataSourceContextHolder.setDataSourceEntry(DataSourceEntry.SHARDING2);
        } else {
            DynamicDataSourceContextHolder.setDataSourceEntry(DataSourceEntry.SHARDING1);
        }
        return userDao.getUserById(id);
    }

}
