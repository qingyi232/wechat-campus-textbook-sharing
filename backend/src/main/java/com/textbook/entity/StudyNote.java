package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("study_note")
public class StudyNote {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private Long majorId;
    private String grade;
    private Long authorId;
    private String noteType;
    private Integer isFree;
    private BigDecimal price;
    private String coverUrl;
    private Integer viewCount;
    private Integer downloadCount;
    private Integer favoriteCount;
    private BigDecimal avgRating;
    private Integer ratingCount;
    private Integer isRecommended;
    private Long recommendTeacherId;
    private String status;
    private String rejectReason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String authorName;
    @TableField(exist = false)
    private String courseName;
    @TableField(exist = false)
    private String majorName;
    @TableField(exist = false)
    private String recommendTeacherName;
    @TableField(exist = false)
    private List<NoteFile> files;
    @TableField(exist = false)
    private Boolean isFavorited;
}
