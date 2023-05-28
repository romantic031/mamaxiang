package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.Dish;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.DishDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DishService extends IService<Dish> {
    ResultVo listByCategory(Dish dish);

    ResultVo saveWithFlavor(DishDto dishDto);

    ResultVo pageByName(PageDto pageDto);

    ResultVo getByIdWithFlavor(Long id);

    ResultVo updateWithFlavor(DishDto dishDto);

    ResultVo updateStatus(int status, List<Long> ids);

    ResultVo deleteDish(List<Long> ids);
}
