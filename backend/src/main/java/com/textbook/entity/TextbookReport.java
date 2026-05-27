package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("textbook_report")
public class TextbookReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long textbookId;
    private Long reporterId;
    private String reason;
    private String status;
    private String handleResult;
    private Long handlerId;
    private LocalDateTime handleTime;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String reporterName;
    @TableField(exist = false)
    private String textbookTitle;
}
