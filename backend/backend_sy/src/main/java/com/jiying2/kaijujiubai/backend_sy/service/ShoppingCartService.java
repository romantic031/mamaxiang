package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.ShoppingCart;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ShoppingCartService extends IService<ShoppingCart> {
    //获取当前登录用户的购物车信息
    ResultVo listShoppingCart();
    //加入商品进入购物车
    ResultVo addShoppingCart(ShoppingCart shoppingCart);
    //从购物车减少或删除商品
    ResultVo subtractShoppingCart(ShoppingCart shoppingCart);
    //清空购物车商品
    ResultVo clean();
}
