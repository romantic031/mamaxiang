package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.ShoppingCart;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.mapper.ShoppingCartMapper;
import com.jiying2.kaijujiubai.backend_sy.service.ShoppingCartService;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public ResultVo listShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, UserHolder.getUser().getId());
        List<ShoppingCart> list = list(lqw);
        if(!list.isEmpty()) {
            return ResultVo.success(list);
        }
        if(list.isEmpty()){
            return ResultVo.success(null,"购物车还没有数据");
        }
        return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"发生错误了");
//        return !list.isEmpty() ? ResultVo.success(list) :ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"购物车还没有数据");
    }

    @Override
    public ResultVo addShoppingCart(ShoppingCart shoppingCart) {
        //1.获取当前线程的用户，指定是哪个用户的购物车数据
        Long userId = UserHolder.getUser().getId();
        shoppingCart.setUserId(userId);
        //2.判断添加的是菜品还是套餐 获取菜品id，如果菜品id不存在则是套餐
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);
        //判断添加的是菜品还是套餐
        if (dishId != null) {
            //添加的是菜品
            lqw.eq(ShoppingCart::getDishId, dishId);
        }
        if(setmealId !=null) {
            //添加的是套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        if(dishId==null&&setmealId==null){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"加入购物车不能为空");
        }
        //3.查询当前菜品或套餐是否存在购物车
        ShoppingCart shoppingcart = getOne(lqw);
        //如果存在数量加一
        if (!StringUtils.isEmpty(shoppingcart)) {
            Integer number = shoppingcart.getNumber();
            shoppingcart.setNumber(number + 1);
            boolean result1 = updateById(shoppingcart);
            return result1 ? ResultVo.success(shoppingcart, "加入购物车成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "加入购物车失败");
//            return result1 ? ResultVo.success(null, "加入购物车成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "加入购物车失败");
        }
        if (StringUtils.isEmpty(shoppingcart)) {
            //如果不存在，则添加到购物车
            shoppingCart.setNumber(1);
            boolean result2 = save(shoppingCart);
            shoppingcart=shoppingCart;
            return result2 ? ResultVo.success(shoppingcart, "加入购物车成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "加入购物车失败");
//            return result2 ? ResultVo.success(null, "加入购物车成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "加入购物车失败");
        }
        return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"操作异常");
    }

    @Override
    public ResultVo subtractShoppingCart(ShoppingCart shoppingCart) {
        Integer dishId1 = query().eq("dish_id", shoppingCart.getDishId()).count();
        Integer setmealId1 = query().eq("setmeal_id", shoppingCart.getSetmealId()).count();
        if(dishId1+setmealId1==0){
            return  ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"操作异常");
        }
        //1.判断此次操作的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        shoppingCart.setUserId(UserHolder.getUser().getId());
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        //2.菜品对应的操作
        if(dishId != null){
            //对应菜品id和用户id，
            lqw.eq(ShoppingCart::getDishId,dishId).eq(ShoppingCart::getUserId,UserHolder.getUser().getId());
            ShoppingCart shoppingCartOne = getOne(lqw);
            System.out.println(shoppingCartOne);
            // 购物车里菜品数量-1 ， 获取-1后的菜品数量
            shoppingCartOne.setNumber(shoppingCartOne.getNumber() - 1 );
            Integer newNumber = shoppingCartOne.getNumber(); //-1后的number
            //如果数量>0就更新操作 ，=0就根据id删除， <0 报异常
            if(newNumber > 0){
                boolean result = updateById(shoppingCartOne);
                return result ? ResultVo.success(shoppingCartOne,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "删除失败");
//                return result ? ResultVo.success(null,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "删除失败");
            }
            if (newNumber == 0){
                boolean result = removeById(shoppingCartOne.getId());
                return result ? ResultVo.success(shoppingCartOne,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "删除失败");
//                return result ? ResultVo.success(null,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "删除失败");
            }
            if (newNumber < 0){
                return  ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"操作异常");
            }
        }
        //3.套餐对应的操作
            //操作与菜品类似
        Long setmealId = shoppingCart.getSetmealId();
        if(setmealId != null){
            lqw.eq(ShoppingCart::getSetmealId,setmealId).eq(ShoppingCart::getUserId,UserHolder.getUser().getId());
            ShoppingCart shoppingCartOne = getOne(lqw);
            shoppingCartOne.setNumber(shoppingCartOne.getNumber() - 1);
            Integer newNumber = shoppingCartOne.getNumber();
            if(newNumber > 0){
                boolean result = updateById(shoppingCartOne);
                return result ? ResultVo.success(shoppingCartOne,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "删除失败");
            }
            if (newNumber == 0){
                boolean result = removeById(shoppingCartOne.getId());
                return result ? ResultVo.success(shoppingCartOne,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "删除失败");
            }
            if (newNumber < 0){
                return  ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"操作异常");
            }
        }
        return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"操作异常");
    }

    @Override
    public ResultVo clean() {
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,UserHolder.getUser().getId());
        boolean result = remove(lqw);
        return result ? ResultVo.success(null,"清空成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"清空失败");
    }

}
