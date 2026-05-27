package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("note_file")
public class NoteFile {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long noteId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private Integer sortOrder;
    private LocalDateTime createTime;
}
