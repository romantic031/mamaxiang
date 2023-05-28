package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_shopping_cart")
public class ShoppingCart {
    @TableId("id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("image")
    private String image;
    @TableField("user_id")
    private Long userId;
    @TableField("dish_id")
    private Long dishId;
    @TableField("setmeal_id")
    private Long setmealId;
    @TableField("dish_flavor")
    private String dishFlavor;
    @TableField("number")
    private Integer number;
    @TableField("amount")
    private BigDecimal amount;
    @TableField("create_time")
    private Date createTime;
}
