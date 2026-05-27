package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("textbook_image")
public class TextbookImage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long textbookId;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
