package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("tb_order_detail")
public class OrderDetail {
    @TableId("id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("image")
    private String image;
    @TableField("order_id")
    private Long orderId;
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
}
