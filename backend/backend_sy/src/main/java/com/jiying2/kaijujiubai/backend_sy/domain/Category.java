package com.jiying2.kaijujiubai.backend_sy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_category")
public class Category {
    @TableId("id")
    private Long id;
    @TableField("type")
    private Integer type;
    @TableField("name")
    private String name;
    @TableField("sort")
    private Integer sort;
}
