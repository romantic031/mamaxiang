package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("tb_setmeal")
public class Setmeal {
    @TableId("id")
    private Long id;
    @TableField("category_id")
    private Long categoryId;
    @TableField("name")
    private String name;
    @TableField("price")
    private BigDecimal price;
    @TableField("status")
    private Integer status;
    @TableField("description")
    private String description;
    @TableField("image")
    private String image;
}
