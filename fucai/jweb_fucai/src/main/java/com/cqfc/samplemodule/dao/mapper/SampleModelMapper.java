package com.cqfc.samplemodule.dao.mapper;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.samplemodule.model.SampleModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface SampleModelMapper extends BaseMapper {

    @Select("SELECT * FROM samplemodel where id= #{modelId}")
    public SampleModel getSampleModelById(@Param("modelId") int id);
	
    @Select("SELECT * FROM samplemodel")
    public List<SampleModel> getSampleModelList();

}
