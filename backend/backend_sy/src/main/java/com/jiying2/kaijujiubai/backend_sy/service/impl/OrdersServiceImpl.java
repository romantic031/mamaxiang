package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.OrderDetail;
import com.jiying2.kaijujiubai.backend_sy.domain.Orders;
import com.jiying2.kaijujiubai.backend_sy.domain.ShoppingCart;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.OrdersDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.UserDto;
import com.jiying2.kaijujiubai.backend_sy.mapper.OrdersMapper;
import com.jiying2.kaijujiubai.backend_sy.service.OrderDetailService;
import com.jiying2.kaijujiubai.backend_sy.service.OrdersService;
import com.jiying2.kaijujiubai.backend_sy.service.ShoppingCartService;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private OrderDetailService orderDetailService;

    @Override
    public ResultVo placeOrder(Orders orders) {
        //1.获取当前用户
        UserDto user = UserHolder.getUser();
        Long userId = user.getId();
        //2.查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lqw);
        //进行判断购物车是否为空
        if (shoppingCartList.isEmpty() || shoppingCartList.size() == 0){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"购物车为空，不能下单");
        }
//        String nickname = user.getNickname();
        long orderId = IdWorker.getId();//订单号 ，IdWorker的方法 ,生成一个orderId，因为是雪花算法还没生成。
        AtomicInteger amount = new AtomicInteger(0);  //原子整型(线程安全)
        //3.遍历购物车数据，以此计算价格总数，同时操作订单明细
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((item) -> {
            //返回订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setImage(item.getImage());
            orderDetail.setName(item.getName());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            if (user.getIsVip()==1){
                orderDetail.setAmount(item.getAmount().multiply(new BigDecimal(0.8)));
            }else{
                orderDetail.setAmount(item.getAmount());
            }
//            BeanUtil.copyProperties(item,orderDetail);
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        //4.订单表操作
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setDineWay(orders.getDineWay());
        orders.setPayMethod(orders.getPayMethod());
//        orders.setRemark(orders.getRemark());

        orders.setPhone(user.getPhone());
        if (user.getIsVip()==1){
            System.out.println(user.getIsVip());
            orders.setAmount(new BigDecimal(amount.get()).multiply(new BigDecimal(0.8)));   //amount变量 总金额 遍历购物车数据
        }else{
            orders.setAmount(new BigDecimal(amount.get()));//amount变量 总金额 遍历购物车数据
        }
        orders.setStoreId(orders.getStoreId());
        boolean result1 = save(orders);
        //5.订单详细表插入数据
        boolean result2 = orderDetailService.saveBatch(orderDetailList);
        //6.删除购物车数据
//        boolean result3 = shoppingCartService.remove(lqw);
        return result1&&result2 ? ResultVo.success(null,"生成订单成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"生成订单失败");
    }

    //后台分页查看 订单详情
    @Override
    public ResultVo pageById(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<Orders> ordersInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders>  lqw=new LambdaQueryWrapper<>();
        //可以根据number 订单号 开始时间、结束时间
        lqw.like(number!=null,Orders::getId,number)
                        .gt(StrUtil.isNotEmpty(beginTime),Orders::getOrderTime,beginTime)
                        .lt(StrUtil.isNotEmpty(endTime),Orders::getOrderTime,beginTime);
        page(ordersInfo,lqw);
        return ResultVo.success(ordersInfo);
    }

    @Override
    public ResultVo userPage(int page, int pageSize) {
        //1.分别创建两个分页对象
        Page<Orders> ordersInfo=new Page<>(page,pageSize);
        Page<OrdersDto> orderDtoInfo=new Page<>();

        LambdaQueryWrapper<Orders> orderslqw=new LambdaQueryWrapper<>();
        orderslqw.orderByDesc(Orders::getOrderTime).eq(Orders::getUserId,UserHolder.getUser().getId());
//        orderslqw.orderByde
        page(ordersInfo,orderslqw);
        //通过订单id返回相应的订单详情
        LambdaQueryWrapper<OrderDetail> orderDetaillqw=new LambdaQueryWrapper<>();
        List<Orders> records = ordersInfo.getRecords();
        String nickname = UserHolder.getUser().getNickname();
        //历史订单 返回多个订单
        List<OrdersDto> ordersDtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            Long orderId = item.getId();
            orderDetaillqw.eq(OrderDetail::getOrderId,orderId);
            List<OrderDetail> orderDetailList = orderDetailService.list(orderDetaillqw);
            BeanUtil.copyProperties(item,ordersDto);
            ordersDto.setOrderDetails(orderDetailList);
            ordersDto.setUsername(nickname);

            return ordersDto;
        }).collect(Collectors.toList());
        //设置orderDto的分页属性和records值
        BeanUtil.copyProperties(ordersInfo,orderDtoInfo,"records");
        orderDtoInfo.setRecords(ordersDtoList);
        //返回OrdersDto
        return ResultVo.success(orderDtoInfo);
    }
}
