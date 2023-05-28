package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.Orders;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrdersService extends IService<Orders> {
    ResultVo placeOrder(Orders orders);

    ResultVo pageById(int page, int pageSize, String number, String beginTime, String endTime);

    ResultVo userPage(int page, int pageSize);
}
