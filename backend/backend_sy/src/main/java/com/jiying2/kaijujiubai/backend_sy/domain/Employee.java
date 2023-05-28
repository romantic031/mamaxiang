package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_employee")
public class Employee {
    @TableId("id")
    private Long id;
    @TableField("account")
    private String account;
    @TableField("name")
    private String name;
    @TableField("password")
    private String password;
    @TableField("phone")
    private String phone;
    @TableField("gender")
    private String gender;
    @TableField("avatar")
    private String avatar;
    @TableField("id_number")
    private String idNumber;
    @TableField("status")
    private Integer status;
    @TableField("identity")
    private Integer identity;

}
