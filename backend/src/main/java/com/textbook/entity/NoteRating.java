package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("note_rating")
public class NoteRating {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long noteId;
    private Long userId;
    private Integer score;
    private String comment;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String userName;
}
