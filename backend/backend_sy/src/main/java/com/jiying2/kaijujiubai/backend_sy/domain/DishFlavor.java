package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_dish_flavor")
public class DishFlavor {
    @TableId("id")
    private Long id;
    @TableField("dish_id")
    private Long dishId;
    @TableField("name")
    private String name;
    @TableField("value")
    private String value;
}
