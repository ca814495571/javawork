package com.cqfc.samplemodule.dao.mapper;

import com.cqfc.samplemodule.protocol.User;
import com.jami.common.dao.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper {

    @Insert({
            "insert into user (lastName, ",
            "firstName, email)",
            "values (#{lastName,jdbcType=VARCHAR}, ",
            "#{firstName,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR})"
    })
    public void addUser(User user);

    @Select("SELECT * FROM user")
    public List<User> getUserList();

    @Delete({
            "delete from user",
            "where id = #{userId,jdbcType=INTEGER}"
    })
    public void deleteUser(Integer userId);

    @Select("SELECT * FROM user where id = #{userId,jdbcType=INTEGER}")
    public User getUserById(int userId);

}
