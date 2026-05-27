package com.textbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.entity.Textbook;
import org.apache.ibatis.annotations.Param;

public interface TextbookMapper extends BaseMapper<Textbook> {

    IPage<Textbook> selectTextbookPage(Page<Textbook> page, @Param("keyword") String keyword,
                                        @Param("courseId") Long courseId, @Param("majorId") Long majorId,
                                        @Param("grade") String grade, @Param("condition") String condition,
                                        @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice,
                                        @Param("status") String status, @Param("sellerId") Long sellerId,
                                        @Param("orderBy") String orderBy);

    Textbook selectDetail(@Param("id") Long id);
}
