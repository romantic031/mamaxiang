package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import com.jiying2.kaijujiubai.backend_sy.domain.DishFlavor;
import com.jiying2.kaijujiubai.backend_sy.domain.Setmeal;
import com.jiying2.kaijujiubai.backend_sy.domain.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
//传回给前端的数据 包括菜品信息和对应的口味信息
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
