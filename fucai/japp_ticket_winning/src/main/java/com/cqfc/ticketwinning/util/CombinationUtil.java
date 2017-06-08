package com.cqfc.ticketwinning.util;

import java.util.ArrayList;
import java.util.List;

public class CombinationUtil {
	// 组合算法
	// 本程序的思路是开一个数组，其下标表示1到m个数，数组元素的值为1表示其下标
	// 代表的数被选中，为0则没选中。
	// 首先初始化，将数组前n个元素置1，表示第一个组合为前n个数。
	// 然后从左到右扫描数组元素值的“10”组合，找到第一个“10”组合后将其变为
	// “01”组合，同时将其左边的所有“1”全部移动到数组的最左端。
	// 当第一个“1”移动到数组的m-n的位置，即n个“1”全部移动到最右端时，就得
	// 到了最后一个组合。
	// 例如求5中选3的组合：
	// 1 1 1 0 0 //1,2,3
	// 1 1 0 1 0 //1,2,4
	// 1 0 1 1 0 //1,3,4
	// 0 1 1 1 0 //2,3,4
	// 1 1 0 0 1 //1,2,5
	// 1 0 1 0 1 //1,3,5
	// 0 1 1 0 1 //2,3,5
	// 1 0 0 1 1 //1,4,5
	// 0 1 0 1 1 //2,4,5
	// 0 0 1 1 1 //3,4,5
	
	/**
	 * 组合
	 * @param balls 组合的数组
	 * @param combineNum 组合的个数
	 * @param separator 组合后连接分隔符
	 * @return
	 */
	public static List<String> combine(String[] balls, int combineNum, String separator) {
		List<String> result = new ArrayList<String>();
		int ballsNum = balls.length;

		StringBuffer sb = new StringBuffer();
		
		int[] bs = new int[ballsNum];
		for (int i = 0; i < ballsNum; i++) {
			bs[i] = 0;
		}

		for (int i = 0; i < combineNum; i++) {
			bs[i] = 1;
		}
		
		//如果组合个数和数组个数相等，则直接添加，返回
		if(ballsNum == combineNum){
			result.add(addBall(bs, balls, separator, sb));
			return result;
		}

		boolean isContinue = true;
		boolean flagRight = true;

		int pos = 0;
		int sum = 0;

		while (isContinue) {
			sum = 0;
			pos = 0;
			flagRight = true;
			result.add(addBall(bs, balls, separator, sb));

			for (int i = 0; i < ballsNum - 1; i++) {
				if (bs[i] == 1 && bs[i + 1] == 0) {
					bs[i] = 0;
					bs[i + 1] = 1;
					pos = i;
					break;
				}
			}

			// 将左边的1全部移动到数组的最左边
			for (int i = 0; i < pos; i++) {
				if (bs[i] == 1) {
					sum++;
				}
			}
			for (int i = 0; i < pos; i++) {
				if (i < sum) {
					bs[i] = 1;
				} else {
					bs[i] = 0;
				}
			}

			// 检查是否所有的1都移动到了最右边
			for (int i = ballsNum - combineNum; i < ballsNum; i++) {
				if (bs[i] == 0) {
					flagRight = false;
					break;
				}
			}
			if (flagRight == false) {
				isContinue = true;
			} else {
				isContinue = false;
				result.add(addBall(bs, balls, separator, sb));
			}

		}

		return result;
	}

	/**
	 * 添加组合内容
	 * @param bs
	 * @param balls
	 * @param separator
	 * @return
	 */
	private static String addBall(int[] bs, String[] balls, String separator, StringBuffer sb) {
		sb.setLength(0);
		for (int i = 0, len = bs.length; i < len; i++) {
			if (bs[i] == 1) {
				sb.append(balls[i]).append(separator);
			}
		}

		return sb.substring(0, sb.length() - separator.length());
	}
	
	public static void main(String[] args) {
    	String[] balls = new String[]{"01", "02", "03", "04", "05", "06", "07"};
    	 
        List<String> l = combine(balls, 2, ",");
        System.out.println("length ---> " + l.size());
        for(int i=0;i<l.size();i++){ 
            System.out.println(l.get(i)); 
        }
	}
}
