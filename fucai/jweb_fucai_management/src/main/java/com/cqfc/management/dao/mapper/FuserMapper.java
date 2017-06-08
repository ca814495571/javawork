package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.management.model.Function;
import com.cqfc.management.model.Fuser;
import com.cqfc.management.model.FuserAndRole;
import com.cqfc.management.model.OperateLog;
import com.cqfc.management.model.Role;
import com.cqfc.management.model.RoleFunction;

public interface FuserMapper extends com.cqfc.common.dao.BaseMapper {

	@Insert("insert into t_fuser (name,password,createTime,active,partnerId) values"
			+ "(#{name},#{password},now(),1,#{partnerId})")
	public int addFuser(Fuser fuser);

	@Update("update t_fuser set active = #{1} where fuserId = #{param1.fuserId} and active =#{param1.active}")
	public int updateFuserActive(Fuser fuser ,int newActive);

	@Update("update t_fuser set name=#{name},loginFailCount=#{loginFailCount},loginTime = #{loginTime},partnerId=#{partnerId} where fuserId=#{fuserId} and active = 1")
	public int updateFuser(Fuser fuser);

	@Update("update t_fuser set password=#{newPassword} where fuserId=#{fuser.fuserId} and password = #{fuser.password} and active = 1")
	public int updateFuserPassword(@Param("fuser") Fuser fuser,
			@Param("newPassword") String newPassword);
	
	@Update("update t_fuser set password=#{fuser.password} where fuserId=#{fuser.fuserId}  and active = 1")
	public int resetFuserPassword(@Param("fuser") Fuser fuser);

	@Select("SELECT LAST_INSERT_ID()")
	public int getCurrentInsertRoleId();
	
	@Select("select * from t_fuser where ${where} and active = 1")
	public Fuser getFuser(@Param("where") String where);

	@Select("select count(*) from t_fuser where ${where} ")
	public int getFuserSumBywhere(@Param("where") String where);

	@Select("select * from t_fuser where ${where} ")
	public List<Fuser> getFuserBywhere(@Param("where") String where);

	@Select("select *  from t_fuser_role where fuserId = #{0}")
	public List<FuserAndRole> getFuserAndRoles(int fuserId);

	
	@Insert("insert into t_fuser_role (fuserId,roleId,createTime) values"
			+ "(#{fuserId},#{roleId},now())")
	public int addFuserAndRole(FuserAndRole fuserAndRole);
	
	
	@Delete("delete from  t_fuser_role where fuserId = #{0} ")
	public int deleteFuserAndRole(int fuserId);
	
	@Delete("delete from  t_fuser_role where roleId = #{0} ")
	public int deleteRoleAndFuser(int roleId);

	@Insert("insert into t_role(name,remark,createTime) value (#{name},#{remark},now()) ")
	public int addRole(Role role);
	
	@Select("select *  from t_role where roleId = #{0}")
	public Role getRole(int roleId);
	
	@Select("select count(*)  from t_role where ${where}")
	public int getRolesNumBywhere(@Param("where")String where);
	
	@Select("select *  from t_role where ${where}")
	public List<Role> getRoles(@Param("where")String where);
	
	@Update("update t_role set name=#{name},remark =#{remark} where roleId=#{roleId}")
	public int updateRole(Role role);
	
	@Delete("delete from t_role where roleId =#{0}")
	public int deleteRole(int roleId);
	
	
	@Insert("insert into t_role_function (roleId,functionId,createTime) values"
			+ "(#{roleId},#{functionId},now())")
	public int addRoloFunction(RoleFunction roleFunction);
	
	
	
	@Delete("delete from  t_role_function where roleId = #{0} ")
	public int deleteRoleAndFunction(int roleId);
	
	@Select("select *  from t_role_function where roleId = #{0} ")
	public List<RoleFunction> getRoleFunctions(int roleId);
	
	@Select("select *  from t_function where functionId = #{0}")
	public Function getFunction(int functionId);
	
	
	@Select("select *  from t_function where functionId = #{0} and level =#{1} ")
	public Function getFunctionByLevel(int functionId,int level);
	
	/**
	 * 查询子节点
	 * @param roleFunction
	 * @return
	 */
	@Select("select *  from t_function where parentId = #{0} and type=#{1} order by sequence asc")
	public List<Function> getChildren(int functionId,int type);
	
	
	
	
	/**
	 * 查询父节点
	 * @param function
	 * @return
	 */
	@Select("select *  from t_function where functionId = #{0}")
	public Function getParent(int parentId);
	
	
	/**
	 * 根据类和主键查询function
	 * @param function
	 * @return
	 */
	@Select("select *  from t_function where functionId=#{0} and type = #{1}")
	public Function getFuntionsByType(int functionId,int type);
	

	
	
	/**
	 * 添加操作日志
	 * @param operateLog
	 * @return
	 */
	@Insert("insert into t_operate_log (operator,fuserId,action,operateTime,type) values("
			+ "#{operator},#{fuserId},#{action},now(),#{type})")
	public int addOperate(OperateLog operateLog);
	
	
	/**
	 * 根据条件查询操作日志
	 * @param where
	 * @return
	 */
	@Select("select * from t_operate_log where ${where}")
	public List<OperateLog> getOperateBywhere(@Param("where")String where);
	
	
	/**
	 * 根据条件查询操作日志
	 * @param where
	 * @return
	 */
	@Select("select count(*) from t_operate_log where ${where}")
	public int getOperateSumBywhere(@Param("where")String where);
	
	/**
	 * 删除当前时间前90天的操作日志
	 * @return
	 */
	@Delete("delete from t_operate_log where operateTime < date_sub(SYSDATE(), interval '90 0:0:0' day_second) limit 2500")
	public int deleteOperateLog();
}
