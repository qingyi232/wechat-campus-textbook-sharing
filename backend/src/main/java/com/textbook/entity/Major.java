package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_major")
public class Major {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String majorName;
    private String majorCode;
    private String department;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
