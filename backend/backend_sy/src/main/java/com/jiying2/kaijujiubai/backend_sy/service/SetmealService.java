package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.Setmeal;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.SetmealDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SetmealService extends IService<Setmeal> {
    ResultVo listByCategory(Long cid);

    ResultVo pageByName(PageDto pageDto);

    ResultVo saveSetmeal(SetmealDto setmealDto);

    ResultVo getByIdWithDish(Long id);

    ResultVo updateWithDish(SetmealDto setmealDto);

    ResultVo removeWithDish(List<Long> ids);

    ResultVo updateStatus(int status, List<Long> ids);
}
