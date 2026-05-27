package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("teacher_recommend")
public class TeacherRecommend {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long teacherId;
    private Long courseId;
    private String textbookTitle;
    private String author;
    private String publisher;
    private String isbn;
    private String edition;
    private String reason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String teacherName;
    @TableField(exist = false)
    private String courseName;
}
