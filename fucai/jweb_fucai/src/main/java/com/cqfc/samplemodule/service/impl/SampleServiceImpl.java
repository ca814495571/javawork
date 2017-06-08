package com.cqfc.samplemodule.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.samplemodule.dao.SampleModelDao;
import com.cqfc.samplemodule.model.SampleModel;
import com.cqfc.samplemodule.service.SampleService;

@Service
public class SampleServiceImpl implements SampleService {

	@Autowired
    private SampleModelDao sampleModelDao;

    @Override
    public SampleModel getSampleModelById(int id) {
        return sampleModelDao.getSampleModelById(id);
    }

    @Override
    public List<SampleModel> getSampleModelList() {
        return sampleModelDao.getSampleModelList();
    }

}
