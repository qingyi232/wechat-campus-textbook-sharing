package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Long textbookId;
    private Integer isRead;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String senderName;
    @TableField(exist = false)
    private String senderAvatar;
    @TableField(exist = false)
    private String receiverName;
    @TableField(exist = false)
    private String textbookTitle;
    @TableField(exist = false)
    private String lastMessage;
    @TableField(exist = false)
    private LocalDateTime lastTime;
    @TableField(exist = false)
    private Integer unreadCount;
}
