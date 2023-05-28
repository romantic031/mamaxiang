package com.jiying2.kaijujiubai.backend_sy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.domain.OrderDetail;
import com.jiying2.kaijujiubai.backend_sy.mapper.OrderDetailMapper;
import com.jiying2.kaijujiubai.backend_sy.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
