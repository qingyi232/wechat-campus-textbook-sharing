package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("textbook")
public class Textbook {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String edition;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private String bookCondition;
    private Long courseId;
    private Long majorId;
    private String grade;
    private String description;
    private Long sellerId;
    private String contactType;
    private String contactInfo;
    private Integer viewCount;
    private Integer favoriteCount;
    private String status;
    private String rejectReason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String sellerName;
    @TableField(exist = false)
    private String courseName;
    @TableField(exist = false)
    private String majorName;
    @TableField(exist = false)
    private List<String> images;
    @TableField(exist = false)
    private Boolean isFavorited;
}
