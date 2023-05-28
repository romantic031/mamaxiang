package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_store")
public class Store {
    @TableId("id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("detail")
    private String detail;
    @TableField("phone")
    private String phone;
    @TableField("business_hours")
    private String businessHours;
}
