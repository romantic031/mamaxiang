package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import com.jiying2.kaijujiubai.backend_sy.domain.Dish;
import com.jiying2.kaijujiubai.backend_sy.domain.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//传回给前端的数据 包括菜品信息和对应的口味信息
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors=new ArrayList<>();
    private String categoryName;
//    private
}
