package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("tb_dish")
public class Dish {
    @TableId("id")
    private Long id;
    @TableField("name")
    private String name;
    @TableField("category_id")
    private Long categoryId;
    @TableField("price")
    private BigDecimal price;
    @TableField("image")
    private String image;
    @TableField("description")
    private String description;
    @TableField("status")
    private Integer status;
    @TableField("sort")
    private Integer sort;
}
