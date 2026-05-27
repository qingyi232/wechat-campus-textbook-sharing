package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("note_favorite")
public class NoteFavorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long noteId;
    private LocalDateTime createTime;
}
