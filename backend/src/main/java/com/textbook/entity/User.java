package com.textbook.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private Long majorId;
    private String grade;
    private String studentNo;
    private String phone;
    private String email;
    private String avatar;
    private String wechat;
    private String bio;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private String majorName;
    @TableField(exist = false)
    private String department;
}
