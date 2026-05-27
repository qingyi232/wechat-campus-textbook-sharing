package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    @Select("SELECT m.*, u.real_name AS sender_name, u.avatar AS sender_avatar " +
            "FROM sys_message m LEFT JOIN sys_user u ON m.sender_id = u.id " +
            "WHERE (m.sender_id = #{userId} AND m.receiver_id = #{targetId}) " +
            "   OR (m.sender_id = #{targetId} AND m.receiver_id = #{userId}) " +
            "ORDER BY m.create_time ASC")
    List<Message> selectChatMessages(@Param("userId") Long userId, @Param("targetId") Long targetId);
}
