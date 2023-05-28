package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_setmeal_dish")
public class SetmealDish {
    @TableId("id")
    private Long id;
    @TableField("setmeal_id")
    private Long setmealId;
    @TableField("dish_id")
    private String dishId;
    @TableField("name")
    private String name;
    @TableField("price")
    private String price;
    //份数
    private Integer copies;
    @TableField("sort")
    private String sort;
}
