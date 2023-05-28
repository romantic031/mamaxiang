package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_user")
public class User {
    @TableId("id")
    private Long id;
    @TableField("account")
    private String account;
    @TableField("password")
    private String password;
    @TableField("phone")
    private String phone;
    @TableField("gender")
    private String gender;
    @TableField("nickname")
    private String nickname;
    @TableField("avatar")
    private String avatar;
    @TableField("status")
    private Integer status;
    @TableField("is_vip")
    private Integer isVip;
}
