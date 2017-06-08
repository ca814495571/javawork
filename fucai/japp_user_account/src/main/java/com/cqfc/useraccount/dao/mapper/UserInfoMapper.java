package com.cqfc.useraccount.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.useraccount.UserAccount;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.UserPreApply;
import com.jami.common.BaseMapper;

/**
 * @author liwh
 */
public interface UserInfoMapper extends BaseMapper {

	/**
	 * 创建用户资料
	 * 
	 * @param userInfo
	 * @return
	 */
	@Insert("insert into t_lottery_user_info${tableIndex}"
			+ "(userId, nickName,cardType,cardNo,mobile,email,userName,sex,birthday,age,userType,registerTerminal,accountType,partnerId,partnerUserId,createTime) "
			+ "values(#{userInfo.userId},#{userInfo.nickName},#{userInfo.cardType},#{userInfo.cardNo},#{userInfo.mobile},#{userInfo.email},#{userInfo.userName},#{userInfo.sex},#{userInfo.birthday},#{userInfo.age},#{userInfo.userType},#{userInfo.registerTerminal},#{userInfo.accountType},#{userInfo.partnerId},#{userInfo.partnerUserId},now())")
	public int createUserInfo(@Param("userInfo") UserInfo userInfo, @Param("tableIndex") String tableIndex);

	/**
	 * 创建用户账户
	 * 
	 * @param userAccount
	 * @return
	 */
	@Insert("insert into t_lottery_user_account${tableIndex} (userId,createTime)"
			+ "values (#{userAccount.userId},now())")
	public int createUserAccount(@Param("userAccount") UserAccount userAccount, @Param("tableIndex") String tableIndex);

	/**
	 * 查询userInfo
	 * 
	 * @param partnerId
	 *            来源渠道ID
	 * @param partnerUserId
	 *            渠道账户ID
	 * @return
	 */
	@Select("select * from t_lottery_user_info${tableIndex} where partnerId=#{partnerId} and partnerUserId=#{partnerUserId}")
	public UserInfo findUserInfoByPartnerId(@Param("partnerId") String partnerId,
			@Param("partnerUserId") String partnerUserId, @Param("tableIndex") String tableIndex);

	/**
	 * 获取当前插入数据用户ID
	 * 
	 * @return
	 */
	@Select("SELECT LAST_INSERT_ID()")
	public int getCurrentInsertUserId();

	@Update("REPLACE INTO t_lottery_user_id(tableName) values('user') ")
	public int updateUserId();

	/**
	 * 更新用户资料
	 * 
	 * @param userInfo
	 * @return
	 */
	@Update("update t_lottery_user_info${tableIndex} set "
			+ "nickName=#{userInfo.nickName},cardType=#{userInfo.cardType},cardNo=#{userInfo.cardNo},mobile=#{userInfo.mobile},email=#{userInfo.email},"
			+ "userName=#{userInfo.userName},sex=#{userInfo.sex},birthday=#{userInfo.birthday},age=#{userInfo.age},userType=#{userInfo.userType},registerTerminal=#{userInfo.registerTerminal},"
			+ "accountType=#{userInfo.accountType} where userId=#{userInfo.userId}")
	public int updateUserInfo(@Param("userInfo") UserInfo userInfo, @Param("tableIndex") String tableIndex);

	/**
	 * 分页查询用户资料
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	@Select("select * from t_lottery_user_info${tableIndex} where ${conditions}")
	public List<UserInfo> getUserInfoList(@Param("conditions") String conditions, @Param("tableIndex") String tableIndex);

	/**
	 * 计算用户资料记录总数
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	@Select("select count(*) from t_lottery_user_info${tableIndex} where ${conditions}")
	public int countUserInfoSize(@Param("conditions") String conditions, @Param("tableIndex") String tableIndex);

	/**
	 * 通过用户ID查询用户账户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@Select("select * from t_lottery_user_account${tableIndex} where userId=#{userId}")
	public UserAccount findUserAccountByUserId(@Param("userId") long userId, @Param("tableIndex") String tableIndex);

	/**
	 * 根据用户ID查询用户资料信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@Select("select * from t_lottery_user_info${tableIndex} where userId=#{userId}")
	public UserInfo findUserInfoById(@Param("userId") long userId, @Param("tableIndex") String tableIndex);

	/**
	 * 更新用户账户（冻结、解冻）
	 * 
	 * @param userId
	 *            用户ID
	 * @param state
	 *            状态 1正常 2冻结
	 * @return
	 */
	@Update("update t_lottery_user_account${tableIndex} set state=#{state} where userId=#{userId}")
	public int modifyUserAccountState(@Param("userId") long userId, @Param("state") int state,
			@Param("tableIndex") String tableIndex);

	/**
	 * 扣除用户帐户资金
	 * 
	 * @param moneySql
	 * @return
	 */
	@Update("update t_lottery_user_account${tableIndex} set totalAmount=totalAmount-#{deductMoney},usableAmount=usableAmount-#{deductMoney} where userId=#{userId} and usableAmount>=#{deductMoney}")
	public int deductUserAccountMoney(@Param("userId") long userId, @Param("deductMoney") long deductMoney,
			@Param("tableIndex") String tableIndex);

	/**
	 * 增加用户帐户资金
	 * 
	 * @param moneySql
	 * @return
	 */
	@Update("update t_lottery_user_account${tableIndex} set totalAmount=totalAmount+#{increaseMoney},usableAmount=usableAmount+#{increaseMoney} where userId=#{userId}")
	public int increaseUserAccountMoney(@Param("userId") long userId, @Param("increaseMoney") long increaseMoney,
			@Param("tableIndex") String tableIndex);

	/**
	 * 冻结账户金额（可用金额、冻结金额）
	 * 
	 * @param userId
	 *            用户ID
	 * @param freezeMoney
	 *            冻结金额
	 * @return
	 */
	@Update("update t_lottery_user_account${tableIndex} set usableAmount=usableAmount-#{freezeMoney},"
			+ "freezeAmount=freezeAmount+#{freezeMoney}" + " where userId=#{userId} and usableAmount>=#{freezeMoney}")
	public int freezeUserAccount(@Param("userId") long userId, @Param("freezeMoney") long freezeMoney,
			@Param("tableIndex") String tableIndex);

	/**
	 * 扣除冻结金额（总金额、冻结金额）
	 * 
	 * @param userId
	 * @param freezeMoney
	 * @return
	 */
	@Update("update t_lottery_user_account${tableIndex} set freezeAmount=freezeAmount-#{freezeMoney},"
			+ "totalAmount=totalAmount-#{freezeMoney} where userId=#{userId}"
			+ " and freezeAmount>=#{freezeMoney} and totalAmount>=#{freezeMoney}")
	public int deductFreezeAmount(@Param("userId") long userId, @Param("freezeMoney") long freezeMoney,
			@Param("tableIndex") String tableIndex);

	/**
	 * 退还冻结金额
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	@Update("update t_lottery_user_account${tableIndex} set freezeAmount=freezeAmount-#{freezeMoney},"
			+ "usableAmount=usableAmount+#{freezeMoney} where userId=#{userId}" + " and freezeAmount>=#{freezeMoney}")
	public int refundFreezeMoney(@Param("userId") long userId, @Param("freezeMoney") long amount,
			@Param("tableIndex") String tableIndex);

	/**
	 * 修改用户账户兑奖密码
	 * 
	 * @param userId
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 */
	@Update("update t_lottery_user_info${tableIndex} set prizePassword=#{newPasswd} where userId=#{userId} and prizePassword=#{oldPasswd}")
	public int updatePrizePassword(@Param("userId") long userId, @Param("oldPasswd") String oldPasswd,
			@Param("newPasswd") String newPasswd, @Param("tableIndex") String tableIndex);

	/**
	 * 创建用户预申请记录
	 * 
	 * @param userPreApply
	 * @return
	 */
	@Insert("insert into t_lottery_user_preapply (partnerId,userId,partnerUniqueNo,preMoney,createTime)"
			+ "values (#{userPreApply.partnerId},#{userPreApply.userId},#{userPreApply.partnerUniqueNo},#{userPreApply.preMoney},now())")
	public int createUserPreApply(@Param("userPreApply") UserPreApply userPreApply);

	/**
	 * 查询预存款申请记录
	 * 
	 * @param partnerId
	 * @param partnerUniqueNo
	 * @return
	 */
	@Select("select * from t_lottery_user_preapply where partnerId=#{partnerId} and partnerUniqueNo=#{partnerUniqueNo}")
	public UserPreApply findUserPreApply(@Param("partnerId") String partnerId, @Param("partnerUniqueNo") String partnerUniqueNo);

	/**
	 * 查询预存款申请列表
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_user_preapply where ${conditions}")
	public List<UserPreApply> getUserPreApplyList(@Param("conditions") String conditions);

	/**
	 * 计算预存款申请记录数
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select count(*) from t_lottery_user_preapply where ${conditions}")
	public int countUserPreApplySize(@Param("conditions") String conditions);

	/**
	 * 通过预存款申请ID查询信息
	 * 
	 * @param preApplyId
	 * @return
	 */
	@Select("select * from t_lottery_user_preapply where preApplyId=#{preApplyId}")
	public UserPreApply findUserPreApplyByPreApplyId(@Param("preApplyId") long preApplyId);

	/**
	 * 更新预存款状态
	 * 
	 * @param preApplyId
	 * @param status
	 * @return
	 */
	@Update("update t_lottery_user_preapply set status=#{status} where preApplyId=#{preApplyId} and status=1")
	public int auditUserPreApply(@Param("preApplyId") long preApplyId, @Param("status") int status);

	/**
	 * 回滚预存款审核
	 * @param preApplyId
	 * @return
	 */
	@Update("update t_lottery_user_preapply set status=1 where preApplyId=#{preApplyId}")
	public int rollbackPreApply(long preApplyId);

	@Select("select sum(totalAmount) from t_lottery_user_account${tableIndex}")
	public Long getTotalAccountMoney(@Param("tableIndex") String tableIndex);
}
