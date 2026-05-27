package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("textbook_comment")
public class TextbookComment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long textbookId;
    private Long userId;
    private String content;
    private Long parentId;
    private Integer status;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userAvatar;
}
