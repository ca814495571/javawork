package com.cqfc.samplemodule.service;


import java.util.List;

import com.cqfc.samplemodule.model.SampleModel;

public interface SampleService {

	List<SampleModel> getSampleModelList();
    SampleModel getSampleModelById(int id);

}
