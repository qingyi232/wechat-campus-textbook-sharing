package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("textbook_favorite")
public class TextbookFavorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long textbookId;
    private LocalDateTime createTime;
}
