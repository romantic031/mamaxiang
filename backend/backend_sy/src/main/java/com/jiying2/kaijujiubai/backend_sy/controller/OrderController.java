package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.Orders;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.service.OrdersService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@Api(tags = "订单接口")
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrdersService ordersService;

    //前台 用户下单
    @PostMapping("/placeOrder")
    public ResultVo placeOrder(@RequestBody Orders orders){
        return ordersService.placeOrder(orders);
    }

    //后台分页查看 订单详情
    @GetMapping("/pageById")
    public ResultVo pageById(int page, int pageSize, String number, String beginTime, String endTime){
        return ordersService.pageById(page,pageSize,number,beginTime,endTime);
    }

    //前台 用户查看订单详情
    @GetMapping("/userPage")
    public ResultVo userPage(int page, int pageSize){
        return ordersService.userPage(page,pageSize);
    }

}
