package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_feedback")
public class UserFeedback {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String content;
    private String contact;
    private String images;
    private String status;
    private String reply;
    private LocalDateTime replyTime;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String userName;
}
