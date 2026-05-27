package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.entity.StudyNote;
import org.apache.ibatis.annotations.Param;

public interface StudyNoteMapper extends BaseMapper<StudyNote> {

    IPage<StudyNote> selectNotePage(Page<StudyNote> page, @Param("keyword") String keyword,
                                     @Param("courseId") Long courseId, @Param("majorId") Long majorId,
                                     @Param("grade") String grade, @Param("noteType") String noteType,
                                     @Param("isFree") Integer isFree, @Param("status") String status,
                                     @Param("authorId") Long authorId, @Param("recommended") Integer recommended,
                                     @Param("orderBy") String orderBy);

    StudyNote selectDetail(@Param("id") Long id);
}
