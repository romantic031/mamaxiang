package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.OrderDetail;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderDetailService extends IService<OrderDetail> {
}
