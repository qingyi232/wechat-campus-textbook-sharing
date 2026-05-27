package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.entity.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    IPage<Course> selectCoursePage(Page<Course> page, @Param("keyword") String keyword,
                                   @Param("majorId") Long majorId, @Param("grade") String grade);

    List<Course> selectByTeacherId(@Param("teacherId") Long teacherId);
}
