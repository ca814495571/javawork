package com.cqfc.test;

import com.cqfc.test.util.CalculatePrize;

public class TestCalculatePrize {

	public static void main(String[] args) {
		
         
		
       CalculatePrize cp = new CalculatePrize();
		
	    cp.makeUpData("D:/calculatePrize.xls",3);
	    for(int i = 0;i<=500;i++){
	    cp.analysisData(i);
	
	    }
	
		  cp.writeDate(cp.getList());
		
	}

}
