package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.Category;
import com.jiying2.kaijujiubai.backend_sy.domain.Dish;
import com.jiying2.kaijujiubai.backend_sy.domain.DishFlavor;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.DishDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.mapper.DishMapper;
import com.jiying2.kaijujiubai.backend_sy.service.CategoryService;
import com.jiying2.kaijujiubai.backend_sy.service.DishFlavorService;
import com.jiying2.kaijujiubai.backend_sy.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private CategoryService categoryService;

//    @Override
//    public ResultVo listByCategory(Long cid) {
//        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
//        //启售的指定分类的菜品
//        lqw.eq(cid!=null,Dish::getCategoryId,cid).eq(Dish::getStatus,1).orderByAsc(Dish::getSort);
//        List<Dish> list = list(lqw);
//        return !list.isEmpty() ? ResultVo.success(list) : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"出错啦");
//    }
@Override
public  ResultVo listByCategory(Dish dish){
    //条件构造器
    LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
    queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
    //添加条件，查询状态为1(起售状态)的菜品
    queryWrapper.eq(Dish::getStatus,1);
    //添加排序条件
    queryWrapper.orderByAsc(Dish::getSort);
    List<Dish> list = list(queryWrapper);  //list把结果变成集合

    List<DishDto> dishDtoList=list.stream().map((item)->{
        DishDto dishDto=new DishDto();    //创建新的dto对象

        //下面的操作只设置了dto中的categoryName属性，而Dish中的属性没有设置
        BeanUtils.copyProperties(item,dishDto); //因为item(records)包括Dish，所以要拷贝

        Long categoryId = item.getCategoryId(); //分类id
        Category category = categoryService.getById(categoryId);  //根据id获取Category对象
        if(category!=null){
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
        }

        //后台 dishFlavor
        Long dishId = item.getId();//dishId
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
        //select * from dish_flavor where dish_id=?
        List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }).collect(Collectors.toList());

    return ResultVo.success(dishDtoList);
}

    @Override
    public ResultVo saveWithFlavor(DishDto dishDto) {
        // 1.保存到dish表并生成id
        boolean save = save(dishDto);
        Long dishId = dishDto.getId();
        // 2.列表中每个DishFlavor只有name和value，需要加入dishId
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        // 3.批量保存对应菜品的口味到口味表
        boolean result = dishFlavorService.saveBatch(flavors);
        return result ? ResultVo.success(null,"新增菜品成功") :ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"新增菜品失败");
    }

    @Override
    public ResultVo pageByName(PageDto pageDto) {
        //1.构造Dish的分页，Dish实体类中只有categoryId，返回需要CategoryName
        Page<Dish> dishInfo=new Page<>(pageDto.getPage(),pageDto.getPageSize());
        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
        lqw.like(StrUtil.isNotEmpty(pageDto.getName()),Dish::getName,pageDto.getName()).orderByAsc(Dish::getSort);

        //2.构造DishDto的分页
        Page<DishDto> dishDtoInfo=new Page<>();
        page(dishInfo,lqw);

        //3.把Dish的分页属性拷贝给DishDto,records不拷贝
//        BeanUtils.copyProperties(DishInfo,DishDtoInfo,"records");
        BeanUtil.copyProperties(dishInfo,dishDtoInfo,"records");
        //4.把Dish的records 复制给DishDto List列表
        List<Dish> records = dishInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //把每个Dish对象复制给DishDto
            BeanUtil.copyProperties(item, dishDto);
            //设置categoryName
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (!StringUtils.isEmpty(category)){
                String name = category.getName();
                dishDto.setCategoryName(name);
            }
            return dishDto;
        }).collect(Collectors.toList());
            dishDtoInfo.setRecords(list);
        //返回DishDto的分页
        return ResultVo.success(dishDtoInfo);
    }

    //可进行多表操作
    @Override
    public ResultVo getByIdWithFlavor(Long id) {
        //1.根据id去Dish表查询菜品
            //复制属性
        Dish dish = getById(id);
        DishDto dishDto=new DishDto();
        BeanUtil.copyProperties(dish,dishDto);
        //2.根据id去DishFlavor表查询菜品的口味
        LambdaQueryWrapper<DishFlavor> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(lqw);
        dishDto.setFlavors(list);
        //返回dishDto
        return ResultVo.success(dishDto);
    }

    @Override
    public ResultVo updateWithFlavor(DishDto dishDto) {
        //1.更新Dish表对应信息
        boolean result1 = updateById(dishDto);

        //2.清除当前菜品对应口味数据
        LambdaQueryWrapper<DishFlavor> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lqw);
        //3.插入当前菜品对应口味数据
            //菜品口味,遍历口味集合，给属性dishId赋值 （因为只有name和value）
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        boolean result2 = dishFlavorService.saveBatch(flavors);
        return result1&&result2 ? ResultVo.success(null,"修改成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"修改失败");
    }

    //通过前端传入要修改的状态，可修改多个菜品的状态
    @Override
    public ResultVo updateStatus(int status, List<Long> ids) {
        //返回指定id的菜品信息，可以多个
        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
        lqw.in(ids!=null,Dish::getId,ids);
        List<Dish> list = list(lqw);
        for(Dish dish:list){
            if(!StringUtils.isEmpty(dish)){
                dish.setStatus(status);
                boolean result = updateById(dish);
            }
        }
        return ResultVo.success(null,"修改成功");
    }

    @Override
    public ResultVo deleteDish(List<Long> ids) {
        //1.列出对应id的列表
        LambdaQueryWrapper<Dish> lqw1=new LambdaQueryWrapper<>();
        lqw1.in(ids!=null,Dish::getId,ids);
        List<Dish> list = list(lqw1);
        for(Dish dish:list){
            //2.//判断是否在售，在售则不给删除
            //判断是否在售，不在售则删除
            if(dish.getStatus() == 0){
                removeById(dish.getId());
                LambdaQueryWrapper<DishFlavor> lqw2=new LambdaQueryWrapper<>();
                lqw2.eq(DishFlavor::getDishId,dish.getId());
                //同时删除口味表的数据
                dishFlavorService.remove(lqw2);
                return ResultVo.success(null,"删除成功");
            }else{
                return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"此菜品还在售卖，不能删除");
            }
        }
        return null;
    }
}
