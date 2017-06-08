package com.cqfc.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cqfc.protocol.businesscontroller.PrintMatch;
import com.cqfc.protocol.businesscontroller.SportPrint;
import com.cqfc.xmlparser.transactionmsg605.Msg;
import com.cqfc.xmlparser.transactionmsg605.Querytype;

/**
 * @author liwh
 */
public class OrderUtil {

	public static String checkPlayType(String lotteryId, String playType) {
		String returnStr = "COMPOUND";
		if (IssueConstant.LOTTERYID_SSQ.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_SSQ_SINGLE.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_QLC.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_QLC_SINGLE.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_FC3D.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_ANDVALUE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_1D.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_2D.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_ANDVALUE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_CHOOSE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_1D.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_2D.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_THREE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_SIX.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_BIG_SMALL.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_THREE_SAME_ALL.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_TRACTOR.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_ODD_EVEN.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_SSC.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_ONESTAR.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_TWOSTAR.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_THREESTAR.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_FIVESTAR.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_SIZE_ODDEVEN.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_FIVESTAR_CHOOSE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_XYNC.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_SHOTS.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_RED.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_GROUP.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_STRAIGHT.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_GROUP.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_STRAIGHT.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_SINGLE.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_SINGLE.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_DLT.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI_ZHUIJIA.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_QXC.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_QXC_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_PLS.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUXUAN_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_PLW.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_PL5_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.LOTTERYID_ZJSYXW.equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_DANSHI.equals(playType)
					|| LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.SportLotteryType.LZC_SF.getValue().equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_LZC_14_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.SportLotteryType.LZC_R9.getValue().equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_LZC_9_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.SportLotteryType.LZC_6BQC.getValue().equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_LZC_6_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		} else if (IssueConstant.SportLotteryType.LZC_4JQS.getValue().equals(lotteryId)) {
			if (LotteryPlayTypeConstants.PLAYTYPE_LZC_4_DANSHI.equals(playType)) {
				returnStr = "SINGLE";
			}
		}
		return returnStr;
	}

	/**
	 * 根据彩种确定数字彩还是竞技彩 1 数字彩 2 竞技彩
	 * 
	 * @param lotteryId
	 * @return
	 */
	public static int getLotteryCategory(String lotteryId) {
		int returnValue = 0;
		if (LotteryType.FC3D.getText().equals(lotteryId) || LotteryType.SSQ.getText().equals(lotteryId)
				|| LotteryType.SSC.getText().equals(lotteryId) || LotteryType.QLC.getText().equals(lotteryId)
				|| LotteryType.XYNC.getText().equals(lotteryId) || LotteryType.DLT.getText().equals(lotteryId)
				|| LotteryType.QXC.getText().equals(lotteryId) || LotteryType.PLS.getText().equals(lotteryId)
				|| LotteryType.PLW.getText().equals(lotteryId) || LotteryType.ZJSYXW.getText().equals(lotteryId)
				|| LotteryType.ZCSFC.getText().equals(lotteryId) || LotteryType.ZCRX9.getText().equals(lotteryId)
				|| LotteryType.ZC4CJQ.getText().equals(lotteryId) || LotteryType.ZC6CBQC.getText().equals(lotteryId)) {
			returnValue = OrderStatus.LotteryType.NUMBER_GAME.getType();
		} else if (LotteryType.JCZQSPF.getText().equals(lotteryId) || LotteryType.JCZQBF.getText().equals(lotteryId)
				|| LotteryType.JCZQJQS.getText().equals(lotteryId) || LotteryType.JCZQBQC.getText().equals(lotteryId)
				|| LotteryType.JCZQHHGG.getText().equals(lotteryId)
				|| LotteryType.JCZQRQSPF.getText().equals(lotteryId) || LotteryType.GYJJC.getText().equals(lotteryId)
				|| LotteryType.JCLQSF.getText().equals(lotteryId) || LotteryType.JCLQRFSF.getText().equals(lotteryId)
				|| LotteryType.JCLQSFC.getText().equals(lotteryId) || LotteryType.JCLQDXF.getText().equals(lotteryId)
				|| LotteryType.JCLQHHGG.getText().equals(lotteryId)|| LotteryType.BDSPF.getText().equals(lotteryId)
				|| LotteryType.BDBF.getText().equals(lotteryId) || LotteryType.BDSXDS.getText().equals(lotteryId)
				|| LotteryType.BDZJQS.getText().equals(lotteryId) || LotteryType.BDBQCSPC.getText().equals(lotteryId)
				|| LotteryType.BDXBC.getText().equals(lotteryId) || LotteryType.BDSFGG.getText().equals(lotteryId)) {
			returnValue = OrderStatus.LotteryType.SPORTS_GAME.getType();
		}
		return returnValue;
	}

	public static int getJcCategoryDetail(String lotteryId) {
		if (LotteryType.JCZQSPF.getText().equals(lotteryId) || LotteryType.JCZQBF.getText().equals(lotteryId)
				|| LotteryType.JCZQJQS.getText().equals(lotteryId) || LotteryType.JCZQBQC.getText().equals(lotteryId)
				|| LotteryType.JCZQHHGG.getText().equals(lotteryId)
				|| LotteryType.JCZQRQSPF.getText().equals(lotteryId)) {
			return OrderStatus.LotteryType.JJZC_GAME.getType();
		} else if (LotteryType.GYJJC.getText().equals(lotteryId) || LotteryType.JCLQSF.getText().equals(lotteryId)
				|| LotteryType.JCLQRFSF.getText().equals(lotteryId) || LotteryType.JCLQSFC.getText().equals(lotteryId)
				|| LotteryType.JCLQDXF.getText().equals(lotteryId) || LotteryType.JCLQHHGG.getText().equals(lotteryId)) {
			return OrderStatus.LotteryType.JJLC_GAME.getType();
		} else if (LotteryType.ZCSFC.getText().equals(lotteryId) || LotteryType.ZCRX9.getText().equals(lotteryId)
				|| LotteryType.ZC4CJQ.getText().equals(lotteryId) || LotteryType.ZC6CBQC.getText().equals(lotteryId)) {
			return OrderStatus.LotteryType.JJLZC_GAME.getType();
		} else if (LotteryType.BDSPF.getText().equals(lotteryId) || LotteryType.BDBF.getText().equals(lotteryId)
				|| LotteryType.BDSXDS.getText().equals(lotteryId) || LotteryType.BDZJQS.getText().equals(lotteryId)
				|| LotteryType.BDBQCSPC.getText().equals(lotteryId) || LotteryType.BDXBC.getText().equals(lotteryId)
				|| IssueConstant.MATCHPLAY_BEIDAN_ALL.equals(lotteryId)) {
			return OrderStatus.LotteryType.JJBD_GAME.getType();
		} else if (LotteryType.BDSFGG.getText().equals(lotteryId)) {
			return OrderStatus.LotteryType.JJBDSFGG_GAME.getType();
		} 
		return 0;
	}

	/**
	 * 竞技彩获取玩法
	 * 
	 * @param playType
	 * @return
	 */
	public static String getSportPlayType(String playType) {
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_JC_DANGUAN)) {
			return "DANGUAN";
		} else {
			return "GUOGUAN";
		}
	}

	public static SportPrint convertMsg2result(Msg msg605, int status) {
		Querytype ticketresult = msg605.getBody().getTicketresult();
		SportPrint result = new SportPrint();
		result.setOrderNo(Long.parseLong(ticketresult.getId()));
		result.setPrintStatus(status);

		result.setPrintTime(msg605.getHead().getTime());
		result.setTicketNo(ticketresult.getOrgserial());
		String odds = ticketresult.getOdds();
		List<PrintMatch> matchList = new ArrayList<PrintMatch>();
		if (!StringUtils.isEmpty(odds)) {
			String[] matchContents = odds.split("/");
			for (int i = 0, len = matchContents.length; i < len; i++) {
				String[] matchSplit = matchContents[i].split("~");
				if (matchSplit.length != 3) {
					continue;
				}
				PrintMatch match = new PrintMatch();
				match.setMatchId(matchSplit[0]);
				match.setTransferId(matchSplit[0]);
				match.setRq("0");
				if (matchSplit[1].length() > 2) {
					match.setSp(matchSplit[1].substring(1, matchSplit[1].length() - 1));
				}
				matchList.add(match);
			}
		}
		result.setMatchList(matchList);
		return result;
	}
	
	public static String getDbNameByOrderNo(String orderNo){
		int len = orderNo.length();
		
		return orderNo.substring(len - 5, len - 3);
	}
	
	public static String getTableNameByOrderNo(String orderNo){
		
		return orderNo.substring(orderNo.length() - 2);
	}	
}
