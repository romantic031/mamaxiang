package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.ShoppingCart;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.service.ShoppingCartService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@Api(tags = "购物车接口")
@RequestMapping("/shoppingCart")
public class ShoppingController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 获取当前登录的用户的购物车信息
     * @return
     */
    @GetMapping("/list")
    public ResultVo listShoppingCart(){
        return shoppingCartService.listShoppingCart();
    }

    /**
     * 加入购物车
     */
    @PostMapping("/add")
    public ResultVo addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.addShoppingCart(shoppingCart);
    }

    /**
     * 从购车中减少或删除
     */
    @PostMapping("/sub")
    public ResultVo subtract(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.subtractShoppingCart(shoppingCart);
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public ResultVo clean(){
        return shoppingCartService.clean();
    }
}
