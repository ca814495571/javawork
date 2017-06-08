
package com.cqfc.samplemodule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.samplemodule.model.SampleModel;
import com.cqfc.samplemodule.service.SampleService;


@Controller
@RequestMapping("/sample")
public class SampleController {

	@Autowired
    private SampleService sampleService;

    @RequestMapping(value = "/list.json")
    @ResponseBody
    public List<SampleModel> list() {
        List<SampleModel> sampleModelList = sampleService.getSampleModelList();
        return sampleModelList;
    }

    @RequestMapping(value = "/list")
    public String getSamplelist(Model model) {
        model.addAttribute("sampleModelList", sampleService.getSampleModelList());
        return "sampleModelList";
    }

}
