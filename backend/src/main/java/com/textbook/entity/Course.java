package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String courseName;
    private String courseCode;
    private Long majorId;
    private String grade;
    private String semester;
    private java.math.BigDecimal credit;
    private String description;
    private Integer sortOrder;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String majorName;
}
