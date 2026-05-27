package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.textbook.entity.NoteRating;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NoteRatingMapper extends BaseMapper<NoteRating> {

    @Select("SELECT r.*, u.real_name AS user_name FROM note_rating r LEFT JOIN sys_user u ON r.user_id = u.id WHERE r.note_id = #{noteId} ORDER BY r.create_time DESC")
    List<NoteRating> selectByNoteId(@Param("noteId") Long noteId);
}
