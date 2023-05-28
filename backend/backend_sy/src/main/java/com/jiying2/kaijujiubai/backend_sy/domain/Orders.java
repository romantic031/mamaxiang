package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_orders")
public class Orders {
    @TableId("id")
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("store_id")
    private Long storeId;
//    @TableField("user_name")
    @TableField("order_time")
    private Date orderTime;
    @TableField("checkout_time")
    private Date checkoutTime;
    @TableField("pay_method")
    private Integer payMethod;
    @TableField("amount")
    private BigDecimal amount;
    @TableField("remark")
    private String remark;
    @TableField("phone")
    private String phone;
    @TableField("status")
    private Integer status;
    @TableField("dine_way")
    private Integer dineWay;
}
