package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String type;
    private Long senderId;
    private Long receiverId;
    private Integer isRead;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String senderName;
}
