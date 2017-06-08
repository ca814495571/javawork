package com.cqfc.statistics.common;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CustomerProperty extends PropertyEditorSupport{

	String format;  
	  
    public String getFormat() {  
        return format;  
    }  
  
    public void setFormat(String format) {  
        this.format = format;  
    }  
  
    // text为需要转换的值，当为bean注入的类型与编辑器转换的类型匹配时就会交给setAsText方法处理  
    public void setAsText(String text) throws IllegalArgumentException {  
        SimpleDateFormat sdf = new SimpleDateFormat(format);  
        try {  
            this.setValue(sdf.parse(text));  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
    }  
}
