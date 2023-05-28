package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.UserVip;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
public interface UserVipService extends IService<UserVip> {
    ResultVo getVipInfo();


    ResultVo rechargeAmount(BigDecimal vipAmount);

    ResultVo becomeVip(int day);
}
