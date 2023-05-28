package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import com.jiying2.kaijujiubai.backend_sy.domain.OrderDetail;
import com.jiying2.kaijujiubai.backend_sy.domain.Orders;
import lombok.Data;

import java.util.List;

/**
 * 返回给前端的订单详情
 */
@Data
public class OrdersDto extends Orders {
    private String username;
    private List<OrderDetail> orderDetails;
}
