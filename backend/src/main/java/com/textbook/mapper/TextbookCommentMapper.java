package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.TextbookComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TextbookCommentMapper extends BaseMapper<TextbookComment> {

    @Select("SELECT c.*, u.real_name AS user_name, u.avatar AS user_avatar FROM textbook_comment c LEFT JOIN sys_user u ON c.user_id = u.id WHERE c.textbook_id = #{textbookId} AND c.status = 1 ORDER BY c.create_time DESC")
    List<TextbookComment> selectByTextbookId(@Param("textbookId") Long textbookId);
}
