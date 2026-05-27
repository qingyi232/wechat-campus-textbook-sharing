package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectUserPage(Page<User> page, @Param("keyword") String keyword,
                               @Param("role") String role, @Param("majorId") Long majorId,
                               @Param("status") Integer status);

    @Select("SELECT u.*, m.major_name, m.department FROM sys_user u LEFT JOIN sys_major m ON u.major_id = m.id WHERE u.id = #{id} AND u.deleted = 0")
    User selectUserWithMajor(@Param("id") Long id);
}
