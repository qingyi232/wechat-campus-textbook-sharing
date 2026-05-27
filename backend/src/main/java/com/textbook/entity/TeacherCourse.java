package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("teacher_course")
public class TeacherCourse {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long teacherId;
    private Long courseId;
    private LocalDateTime createTime;
}
