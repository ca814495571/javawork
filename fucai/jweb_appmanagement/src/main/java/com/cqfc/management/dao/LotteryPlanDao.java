package com.cqfc.management.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.LotteryPlanMapper;
import com.cqfc.management.model.LotteryPlan;

@Repository
public class LotteryPlanDao {

	@Autowired
	private LotteryPlanMapper lotteryPlanMapper;

	public int inertLotteryPlan(LotteryPlan lotteryPlan) {

		filterNullColumn(lotteryPlan);

		return lotteryPlanMapper.insert(lotteryPlan);
	}

	public int getLotterySaleByType(String enterTag, String date,
			String lotteryType) {

		return lotteryPlanMapper.getLotterySaleByType(enterTag, date,
				lotteryType);
	}

	public List<LotteryPlan> getByPlanId(LotteryPlan lotteryPlan){
		
		return lotteryPlanMapper.getByPlanId(lotteryPlan);
	}
	
	private void filterNullColumn(LotteryPlan lotteryPlan) {

		if (lotteryPlan.getPlanId() == null
				|| "NULL".equals(lotteryPlan.getPlanId())
				|| "null".equals(lotteryPlan.getPlanId())) {

			lotteryPlan.setPlanId("");
		}

		if (lotteryPlan.getUserId() == null
				|| "NULL".equals(lotteryPlan.getUserId())
				|| "null".equals(lotteryPlan.getUserId())) {

			lotteryPlan.setUserId("");
		}

		if (lotteryPlan.getLotteryId() == null
				|| "NULL".equals(lotteryPlan.getLotteryId())
				|| "null".equals(lotteryPlan.getLotteryId())) {

			lotteryPlan.setLotteryId("");
		}

		if (lotteryPlan.getLotteryName() == null
				|| "NULL".equals(lotteryPlan.getLotteryName())
				|| "null".equals(lotteryPlan.getPlanId())) {

			lotteryPlan.setLotteryName("");
		}

		if (lotteryPlan.getTotalAmount() == null
				|| "NULL".equals(lotteryPlan.getTotalAmount())
				|| "null".equals(lotteryPlan.getTotalAmount())) {

			lotteryPlan.setTotalAmount("");
		}

		if (lotteryPlan.getCreateTime() == null
				|| "NULL".equals(lotteryPlan.getCreateTime())
				|| "null".equals(lotteryPlan.getCreateTime())) {

			lotteryPlan.setCreateTime("");
		}

		if (lotteryPlan.getCharOne() == null
				|| "NULL".equals(lotteryPlan.getCharOne())
				|| "null".equals(lotteryPlan.getCharOne())) {

			lotteryPlan.setCharOne("");
		}

		if (lotteryPlan.getCharTwo() == null
				|| "NULL".equals(lotteryPlan.getCharTwo())
				|| "null".equals(lotteryPlan.getCharTwo())) {

			lotteryPlan.setCharTwo("");
		}

		if (lotteryPlan.getExtInfo() == null
				|| "NULL".equals(lotteryPlan.getExtInfo())
				|| "null".equals(lotteryPlan.getExtInfo())) {

			lotteryPlan.setExtInfo("");
		}
	}

}
