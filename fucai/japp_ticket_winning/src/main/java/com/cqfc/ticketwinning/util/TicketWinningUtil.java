package com.cqfc.ticketwinning.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.cqfc.util.LotteryType;

public class TicketWinningUtil {
	private static final Pattern pattern = Pattern.compile("[0-9]{13}"); 
	
	/**
	 * 升序排序(1,3,2 --> 1,2,3)
	 * @param ballContent
	 * @param splitStr
	 * @return
	 */
	public static String sortBallContent(String ballContent, String splitStr){
		String retStr = "";
		String[] balls = ballContent.split(splitStr);
		
		for(int i = 0; i < balls.length; i++){
			for(int j = 0; j < i; j++){
				if(Integer.valueOf(balls[j]) > Integer.valueOf(balls[i])){
					String temp = balls[j];
					balls[j] = balls[i];
					balls[i] = temp;
				}
			}
		}
		int k = 0;
		for(; k < balls.length - 1; k++){
			retStr += balls[k] + splitStr;
		}
		retStr += balls[k];
		return retStr;
	}
	
	/**
	 * 计算排列 n！= 1 * 2 * 3 * ... * ( n - 1) * n
	 * @param num
	 * @return
	 */
	public static int getPermNum(int num){		
		if(num == 0 || num == 1){
			return 1;
		}
		
		return num * getPermNum(num - 1);
	}
	
	/**
	 * 计算组合 C(n, m) = n! / ((n - m)! * m!) = n * (n - 1) ... (n - m) / m * (m - 1) * 1
	 * @param n
	 * @param m
	 * @return
	 */
	public static int getCombNum(int n, int m){
		if(n < m){
			return 0;
		}
		if(n == m){
			return 1;
		}
		
		int temp = n - m;
		int nCount = 1;
		int mCount = 1;
		
		if(m >= temp){
			for(int i=n; i>m; i--){
				nCount *= i;
			}
			for(int i=temp; i>1; i--){
				mCount *= i;
			}
		}else{
			for(int i=n; i>temp; i--){
				nCount *= i;
			}				
			for(int i=m; i>1; i--){
				mCount *= i;
			}
		}
		return nCount / mCount;
	}
	
	/**
	 * 计算数组中n个数相乘后求和，如[1,2,3]*2=1*2+1*3+2*3
	 * @param balls
	 * @param n
	 * @return
	 */
	public static int getArrMul(int[] ballCounts, int n){
		int sum = 0;
		
		String[] balls = TicketWinningUtil.int2StrArr(ballCounts);
		List<String> combs = CombinationUtil.combine(balls, n, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		for(String comb : combs){
			int[] combInts = TicketWinningUtil.str2IntArr(comb.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
			int tmp = 1;
			for(int i = 0, len = combInts.length; i < len; i++){
				tmp *= combInts[i];
			}
			sum += tmp;
		}
		
		return sum;
	}
	
	/**
	 * 判断投注数符合要求
	 * @param num
	 * @param minNum
	 * @param maxNum
	 * @return
	 */
	public static boolean isBallCount(int num, int minNum, int maxNum){
		if(num < minNum || num > maxNum){
			return false;
		}
		return true;
	}		
	
	/**
	 * 判断时时彩投注大小单双的球符合要求
	 * @param ballContent
	 * @param separator
	 * @return
	 */
	public static boolean isSSCSizeOddEvenBallNum(String ballContent, String separator){
		boolean flag = true;
		String[] balls = ballContent.split(separator);
		
		for(int i = 0; i < balls.length; i++){
			if(TicketWinningConstantsUtil.CONTENT_SSC_SIZE_ODDEVEN.indexOf(balls[i]) == -1){
				flag = false;
				break;
			}	
		}
		
		return flag;
	}
	
	/**
	 * 判断球是否重复
	 * @param ballContent
	 * @param separator
	 * @return
	 */
	public static boolean isBallContentRepeat(String ballContent, String separator){
		boolean flag = true;
		
		String[] balls = ballContent.split(separator);
		Set<String> setStr = new HashSet<String>();
		for(int i = 0; i < balls.length; i++){
			setStr.add(balls[i]);
		}
		
		if(balls.length != setStr.size()){
			flag = false;
		}
		
		return flag;
	}	
	
	/**
	 * 福彩3D单选和值
	 * @param num
	 * @return
	 */
	public static int getFC3DRadioAndValue(int num){
		int[] andValue = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1};
		
		return andValue[num];
	}
	
	/**
	 * 福彩3D组三和值
	 * @param num
	 * @return
	 */
	public static int getFC3DGroup3AndValue(int num){
		int[] andValue = {1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5, 5, 4, 5, 5, 4, 5, 4, 3, 3, 3, 1, 2, 1};
		
		return andValue[num - 1];
	}
	
	/**
	 * 福彩3D组六和值
	 * @param num
	 * @return
	 */
	public static int getFC3DGroup6AndValue(int num){
		int[] andValue = {1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9, 8, 7, 5, 4, 3, 2, 1, 1};
		
		return andValue[num - 3];
	}
	
	
	/**
	 * 时时彩二星组选包点
	 * @param num
	 * @return
	 */
	public static int getSSCTwoStarGroupAndValue(int num){
		int[] andValue = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1};
		
		return andValue[num];
	}
	
	/**
	 * 时时彩二星包点
	 * @param num
	 * @return
	 */
	public static int getSSCTwoStarAndValue(int num){
		int[] andValue = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
		
		return andValue[num];
	}
	
	/**
	 * 时时彩三星包点
	 * @param num
	 * @return
	 */
	public static int getSSCThreeStarAndValue(int num){
		int[] andValue = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1};
		
		return andValue[num];
	}
	
	/**
	 * 时时彩组三包点
	 * @param num
	 * @return
	 */
	public static int getSSCThreeStarGroupAndValue(int num){
		int[] andValue = {1, 1, 2, 3, 4, 5, 7, 8, 10, 12, 13, 14, 15, 15, 15, 15, 14, 13, 12, 10, 8, 7, 5, 4, 3, 2, 1, 1};
		
		return andValue[num];
	}
	
	/**
	 * 福彩3D单选和值内容
	 * @param num
	 * @return
	 */
	public static List<String> getFC3DRadioAndValueDetail(int num){
	    List<String> details = new ArrayList<String>();	   

	    for(int i = 0; i <= 9; i++){
		   for(int j = 0; j <= 9; j++){
			   for(int k = 0; k <= 9; k++){
				   if((i + j + k) == num){						  
					   details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + k);
				   }
			   }
		   }
	    }
	   
	    return details;		
	}
	
	/**
	 * 组选三包号或复式
	 * @param content
	 * @param separator
	 * @return
	 */
	public static List<String> getGroup3Compound(String content, String separator, String connector){
		List<String> details = new ArrayList<String>();
		String[] balls = content.split(separator);
		
		for(int i = 0; i < balls.length; i++){
			for(int j = 0; j < balls.length; j++){
				if(balls[i] != balls[j]){
					details.add(balls[i] + connector + balls[i] + connector + balls[j]);
				}
			}
		}
		
		return details;
	}
	
	/**
	 * 组选三包点或和值
	 * @param content
	 * @return
	 */
	public static List<String> getGroup3AndValue(String content){
		List<String> details = new ArrayList<String>();
		int sum = Integer.valueOf(content);
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if((i != j) && (2 * i + j == sum)){
					details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j);
				}
			}
		}
		return details;
	}
	
	/**
	 * 排列3直选和值
	 * @param num
	 * @return
	 */
	public static int getPL3ZhiXuanHeZhi(int num){
		int[] andValue = {1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1};
		
		return andValue[num];
	}
	
	/**
	 * 排列3组三和值
	 * @param num
	 * @return
	 */
	public static int getPL3ZuSanHeZhi(int num){
		int[] andValue = {1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5, 5, 4, 5, 5, 4, 5, 4, 3, 3, 3, 1, 2, 1};
		
		return andValue[num - 1];
	}
	
	/**
	 * 排列3组六和值
	 * @param num
	 * @return
	 */
	public static int getPL3ZuLiuHeZhi(int num){
		int[] andValue = {1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9, 8, 7, 5, 4, 3, 2, 1, 1};
		
		return andValue[num - 3];
	}
	
	
	/**
	 * 排列3直选组合
	 * @param num
	 * @return
	 */
	public static List<String> getPL3ZhiXuanZuHe(int num) {
	    List<String> details = new ArrayList<String>();	   

	    for(int i = 0; i <= 9; i++){
		   for(int j = 0; j <= 9; j++){
			   for(int k = 0; k <= 9; k++){
				   if((i + j + k) == num){						  
					   details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + k);
				   }
			   }
		   }
	    }
	   
	    return details;		
	}
	
	/**
	 * 排列3组六和值
	 * @param content
	 * @return
	 */
	public static List<String> getPL3ZuLiuHeZhiDetails(String content){
		int sum = Integer.valueOf(content);
		List<String> details = new ArrayList<String>();	 
		
		for(int i = 0; i < 10; i++){
			for(int j = i + 1; j < 10; j++){
				for(int k = j + 1; k < 10; k++){
					if((i + j + k) == sum){
						details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + k);
					}
				}
			}
		}
		
		return details;
	}
	
	/**
	 * 反转字符串
	 * @param str
	 * @return
	 */
	public static String reverseString(String str){
		
		return new StringBuffer(str).reverse().toString();
	}
	
	/**
	 * 判断数组是否重复
	 * @param ballsInt
	 * @return
	 */
	public static boolean isBallContentNoRepeat(int[] ballsInt) {
		Set<Integer> balls = new HashSet<Integer>();
		
		int ballsIntLen = ballsInt.length;
		for(int i = 0; i < ballsIntLen; i++){
			balls.add(ballsInt[i]);
		}
		
		if(balls.size() != ballsIntLen){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 判断数组是否处于min(包含)和max(包含)之间
	 * @param ballsInt
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isBallContentInRange(int[] ballsInt, int min,
			int max) {
		int i = 0, ballTemp = 0;
		
		for(int len = ballsInt.length; i < len; i++){
			ballTemp = ballsInt[i];
			if((ballTemp < min) || (ballTemp > max)){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 判断数组是否不重复且内容处于min(包含)和max(包含)之间
	 * @param ballsInt
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isBallContentNoRepeatAndInRange(int[] ballsInt, int min,
			int max) {
		Set<Integer> balls = new HashSet<Integer>();
		
		int ballsIntLen = ballsInt.length;
		int tempInt = 0;
		for(int i = 0; i < ballsIntLen; i++){
			tempInt = ballsInt[i];
			if(tempInt < min || tempInt > max){
				return false;
			}
			else{
				balls.add(tempInt);				
			}
		}
		
		if(balls.size() != ballsIntLen){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 判断数组是否升序且内容处于min(包含)和max(包含)之间
	 * @param ballsInt
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isBallContentASCAndInRange(int[] ballsInt, int min,
			int max) {
		int i = 0, ballTemp = 0;
		
		for(int len = ballsInt.length; i < len - 1; i++){
			ballTemp = ballsInt[i];
			if((ballTemp > ballsInt[i+1]) || (ballTemp < min) || (ballTemp > max)){
				return false;
			}
		}
		
		if(ballsInt[i] < min || ballsInt[i] > max){
			return false;
		}
		
		return true;
	}
	
	/**
	 * 判断数组是否不重复且升序且内容处于min(包含)和max(包含)之间
	 * @param ballsInt
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isBallContentNoRepeatAndASCAndInRange(int[] ballsInt, int min,
			int max) {
		int i = 0, ballTemp = 0;
		
		for(int len = ballsInt.length; i < len - 1; i++){
			ballTemp = ballsInt[i];
			if((ballTemp >= ballsInt[i+1]) || (ballTemp < min) || (ballTemp > max)){
				return false;
			}
		}
		
		if(ballsInt[i] < min || ballsInt[i] > max){
			return false;
		}
		
		return true;
	}
	
	/**
	 * string数组转换为int数组
	 * @param balls
	 * @return
	 */
	public static int[] str2IntArr(String[] balls){
		int len = balls.length;
		int[] retIntArr = new int[len];
		
		for(int i = 0; i < len; i++){
			retIntArr[i] = Integer.valueOf(balls[i]);
		}
		
		return retIntArr;
	}
	
	
	/**
	 * int数组转换为string数组
	 * @param balls
	 * @return
	 */
	public static String[] int2StrArr(int[] balls){
		int len = balls.length;
		String[] retStrArr = new String[len];
		
		for(int i = 0; i < len; i++){
			retStrArr[i] = String.valueOf(balls[i]);
		}
		
		return retStrArr;
	}
	
	/**
	 * 判断小于10的球前缀是否带有0,如果全部有则返回true
	 * @param balls
	 * @return
	 */
	public static boolean isAllHasZeroPrefixLessThanTen(String[] balls){
		boolean flag = true;
		
		for(int i = 0, len = balls.length; i < len; i++){
			if(Integer.valueOf(balls[i]) < 10 && !balls[i].startsWith("0")){
				flag = false;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 判断小于10的球前缀是否带有0,如果有则返回true
	 * @param ball
	 * @return
	 */
	public static boolean isHasZeroPrefixLessThanTen(String ball){
		boolean flag = true;
		
		if((Integer.valueOf(ball) >= 10) || (Integer.valueOf(ball) < 10 && ball.length() == 1)){
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 判断小于10的球前缀是否带有0,如果有则返回true
	 * @param balls
	 * @return
	 */
	public static boolean isAnyHasZeroPrefixLessThanTen(String[] balls){
		boolean flag = false;
		
		for(int i = 0, len = balls.length; i < len; i++){
			if(Integer.valueOf(balls[i]) < 10 && balls[i].length() == 2 && balls[i].startsWith("0")){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 判断两个数组中是否含有相同元素
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static boolean isTwoArrContainSameElement(int[] arr1, int[] arr2){
		boolean flag = false;
		Set<Integer> setInt = new HashSet<Integer>();
		int len1 = arr1.length;
		int len2 = arr2.length;
		
		for(int i = 0; i < len1; i++){
			setInt.add(arr1[i]);
		}
		
		for(int i = 0; i < len2; i++){
			setInt.add(arr2[i]);
		}
		
		if((len1 + len2) != setInt.size()){
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * 计算中奖赔率
	 * @param odds
	 * @param winningContent
	 * @return
	 */
	public static int computeWinOdds(String odds, String winningContent){
		int retValue = 0;
		
		String[] oddsSplit = odds.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		for(int i = 0, len = oddsSplit.length; i < len; i++){
			String[] contentSplit = oddsSplit[i].split(TicketWinningConstantsUtil.SEPARATOR_AT);
			if(contentSplit[0].equals(winningContent)){
				retValue = (int)(Double.valueOf(contentSplit[1]) * 100);
			}
		}
		
		return retValue;
	}
	
	/**
	 * 计算M串N组合中奖赔率
	 * @param winOddsList
	 * @param m
	 * @return
	 */
	public static int computeMXNWinOdds(List<Integer> winOddsList, int m){
		BigInteger retValue = new BigInteger("1");
		String[] winOddsBalls = new String[winOddsList.size()];
		
		for(int i = 0, len = winOddsList.size(); i < len; i++){
			winOddsBalls[i] = String.valueOf(winOddsList.get(i));
		}
		List<String> combs = CombinationUtil.combine(winOddsBalls, m, TicketWinningConstantsUtil.SEPARATOR_AT);
		
		for(String comb : combs){
			String[] combSplit = comb.split(TicketWinningConstantsUtil.SEPARATOR_AT);
			for(int j = 0, lenSplit = combSplit.length; j < lenSplit; j++){
				retValue = retValue.multiply(new BigInteger(combSplit[j]));
			}
		}
		if(retValue.equals(new BigInteger("0"))){
			return 0;
		}
		int tempRet = retValue.multiply(new BigInteger("10")).divide(new BigInteger("100").pow(m -1)).intValue();
		
		int mod = tempRet % 10;
		int ret = tempRet / 10;

		if(mod > 4){
			ret += 1;
		}
		return ret;
	}
	
	/**
	 * 判断num是否为0,1,3中一个
	 * @param num
	 * @return
	 */
	public static boolean isEqualZeroOneThree(int num){
		if(num == 0 || num == 1 || num == 3){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断num是否为0,1,3中一个
	 * @param numStr
	 * @return
	 */
	public static boolean isAnyNotEqualZeroOneThree(String numStr){
		int num = Integer.valueOf(numStr);
		if((num != 0) || (num != 1) || (num != 3)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 计算组合内容
	 * @param contentList
	 * @param content
	 * @return
	 */
	public static List<String> calContentList(List<String> contentList, String content){
		List<String> retList = new ArrayList<String>();
		String[] contentSplit = content.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		if(contentList == null || contentList.size() == 0){
			for(int i = 0, len = contentSplit.length; i < len; i++){
				retList.add(contentSplit[i]);
			}
		}
		else {
			for(String str : contentList){
				for(int i = 0, len = contentSplit.length; i < len; i++){
					retList.add(str + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + contentSplit[i]);
				}
			}			
		}
		
		return retList;
	}
	
	/**
	 * 判断matchId是否由13位数字组成
	 * @param matchId
	 * @return
	 */
	public static boolean isMatchIdNumeric(String matchId){
		  
		 return pattern.matcher(matchId).matches(); 		
	}
	
	/**
	 * 拆解混合过关中奖内容
	 * @param winningContent
	 * @return
	 */
	public static Map<String, String> getWinningContentMap(String winningContent){
		Map<String, String> contentMap = new HashMap<String, String>();
		String[] content = winningContent.split(TicketWinningConstantsUtil.SEPARATOR_AT);
		
		for(int i = 0, len = content.length; i < len; i++){
			String[] contentSplit = content[i].split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			contentMap.put(contentSplit[0], contentSplit[1]);
		}
		
		return contentMap;
	}
	
	public static int[] getBallsMXNByLotteryId(String lotteryId, int m, int n){
		if(lotteryId.equals(LotteryType.BDBF.getText())){
			return BDBFMXNUtil.mxn(m, n);
		}
		else if(lotteryId.equals(LotteryType.BDSFGG.getText())){
			return BDSFGGMXNUtil.mxn(m, n);
		}
		else if(lotteryId.equals(LotteryType.BDSPF.getText())){
			return BDSPFMXNUtil.mxn(m, n);
		}
		else if(lotteryId.equals(LotteryType.BDZJQS.getText()) || lotteryId.equals(LotteryType.BDBQCSPC.getText()) 
				|| lotteryId.equals(LotteryType.BDSXDS.getText())){
			return  BDZJQSAndBQCAndSXDSMXNUtil.mxn(m, n);
		}
		return null;
	}
	
	/**
	 * 判断北单玩法是否存在
	 * @param lotteryId
	 * @param playType
	 * @return
	 */
	public static boolean isBDPlayTypeExist(String lotteryId, String playType){			
		if(lotteryId.equals(LotteryType.BDSPF.getText())){
			return BDSPFMXNUtil.isPlayTypeExist(playType);
		}
		else if(lotteryId.equals(LotteryType.BDBF.getText())){
			return BDBFMXNUtil.isPlayTypeExist(playType);
		}
		else if(lotteryId.equals(LotteryType.BDSFGG.getText())){
			return BDSFGGMXNUtil.isPlayTypeExist(playType);
		}
		else if(lotteryId.equals(LotteryType.BDZJQS.getText()) 
				|| lotteryId.equals(LotteryType.BDBQCSPC.getText())
				|| lotteryId.equals(LotteryType.BDSXDS.getText())){
			return BDZJQSAndBQCAndSXDSMXNUtil.isPlayTypeExist(playType);
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		System.out.println(isMatchIdNumeric("123456789012"));
	}


}
