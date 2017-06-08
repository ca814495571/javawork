package com.cqfc.samplemodule.dao;

import java.util.List;

import com.cqfc.samplemodule.dao.mapper.SampleModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.samplemodule.model.SampleModel;

@Repository
public class SampleModelDao {

    @Autowired
    SampleModelMapper sampleModelMapper;

    public SampleModel getSampleModelById(int id) {
        return sampleModelMapper.getSampleModelById(id);
    }

    public List<SampleModel> getSampleModelList() {
        return sampleModelMapper.getSampleModelList();
    }

}
