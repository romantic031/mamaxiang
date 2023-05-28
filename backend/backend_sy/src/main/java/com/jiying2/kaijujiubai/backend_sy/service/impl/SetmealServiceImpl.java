package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.Category;
import com.jiying2.kaijujiubai.backend_sy.domain.Dish;
import com.jiying2.kaijujiubai.backend_sy.domain.Setmeal;
import com.jiying2.kaijujiubai.backend_sy.domain.SetmealDish;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.SetmealDto;
import com.jiying2.kaijujiubai.backend_sy.mapper.SetmealMapper;
import com.jiying2.kaijujiubai.backend_sy.service.CategoryService;
import com.jiying2.kaijujiubai.backend_sy.service.SetmealDishService;
import com.jiying2.kaijujiubai.backend_sy.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private CategoryService categoryService;
    @Resource
    private SetmealDishService setmealDishService;
    @Override
    public ResultVo listByCategory(Long cid) {
        LambdaQueryWrapper<Setmeal> lqw=new LambdaQueryWrapper<>();
        lqw.eq(cid!=null,Setmeal::getCategoryId,cid).eq(Setmeal::getStatus,1);
        List<Setmeal> list = list(lqw);
        return !list.isEmpty() ? ResultVo.success(list) : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"查询套餐失败");
    }

    /**
     * 不显示套餐的菜品具体信息
      * @param pageDto
     * @return
     */
    @Override
    public ResultVo pageByName(PageDto pageDto) {
        //1.分页构造器对象
            //Setmeal分页对象
        Page<Setmeal> semealInfo=new Page<>(pageDto.getPage(),pageDto.getPageSize());
            //根据名字模糊查询
        LambdaQueryWrapper<Setmeal> lqw=new LambdaQueryWrapper<>();
        lqw.like(StrUtil.isNotEmpty(pageDto.getName()),Setmeal::getName,pageDto.getName());
        page(semealInfo,lqw);
            //SetmealDto分页对象
        Page<SetmealDto> setmealDtoInfo=new Page<>();

        //2.对象拷贝
            //把Setmeal的分页属性拷贝给SetmealDto,records不拷贝
        BeanUtil.copyProperties(semealInfo,setmealDtoInfo,"records");
        List<Setmeal> records = semealInfo.getRecords();

        //3.把整个的返回结果赋给List<SetmealDto>
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto=new SetmealDto();
            //把每个Setmeal对象复制
            BeanUtil.copyProperties(item, setmealDto);
            //设置categoryName
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (!StringUtils.isEmpty(category)){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        //4.给SetmealDto分页对象设置records
            setmealDtoInfo.setRecords(list);
            //返回SetmealDto
        return ResultVo.success(setmealDtoInfo);
    }

    @Override
    public ResultVo saveSetmeal(SetmealDto setmealDto) {
        //1.保存套餐的基本信息，setmeal表
        Integer name = query().eq("name", setmealDto.getName()).count();
        if(name>0){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"当前套餐名字已存在");
        }
        boolean result1 = save(setmealDto);
//        System.out.println(result1);
        //2.SetmealDish缺少setmealId，save后会生成id，设置setmealId
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return  item;
        }).collect(Collectors.toList());

        //3.保存套餐和菜品关联信息，setmeal_dish表
        boolean result2 = setmealDishService.saveBatch(setmealDishes);
        return result1&&result2 ? ResultVo.success(null,"新增套餐成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"新增套餐失败");
    }

    @Override
    public ResultVo getByIdWithDish(Long id) {
        //1.根据套餐的id查找Setmeal对应的信息
        Setmeal setmeal = getById(id);
        //2.创建SetmealDto对象 并设置相应属性
        SetmealDto setmealDto=new SetmealDto();
        BeanUtil.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(lqw);
        setmealDto.setSetmealDishes(setmealDishes);

        //返回SetmealDto
        return ResultVo.success(setmealDto);
    }

    @Override
    public ResultVo updateWithDish(SetmealDto setmealDto) {
        //1.更新setmeal表基本信息
        boolean result1 = updateById(setmealDto);
        //2.清除当前套餐菜品对应表setmeal_dish表的数据，根据setmealId
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(lqw);
        //3.插入setmeal_dish对应的数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            //其中setmealId需要设置
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        boolean result2 = setmealDishService.saveBatch(setmealDishes);
        System.out.println(result1);
        System.out.println(result2);
        return result1&&result2 ? ResultVo.success(null,"修改套餐信息成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"修改信息失败");
    }

    @Override
    public ResultVo removeWithDish(List<Long> ids) {
        //1.删除setmeal套餐表
            //判断status状态是否在售
        LambdaQueryWrapper<Setmeal> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getStatus,1).in(Setmeal::getId,ids);
        int count = count(lqw);
        if(count>0){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"该套餐还在售卖，不能删除");
        }
        boolean result1 = removeByIds(ids);
        //2.删除setmeal_dish套餐和菜品关系表
        LambdaQueryWrapper<SetmealDish> lqw2=new LambdaQueryWrapper<>();
        lqw2.in(SetmealDish::getSetmealId,ids);
        boolean result2 = setmealDishService.remove(lqw2);
        return result1 && result2 ? ResultVo.success(null,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"删除失败");
    }

    @Override
    public ResultVo updateStatus(int status, List<Long> ids) {
        //返回指定id的套餐信息，可以多个
        LambdaQueryWrapper<Setmeal> lqw=new LambdaQueryWrapper<>();
        lqw.in(ids!=null,Setmeal::getId,ids);
        List<Setmeal> list = list(lqw);
        for(Setmeal setmeal:list){
            if(!StringUtils.isEmpty(setmeal)){
                //其中传入的status是反值
                setmeal.setStatus(status);
                boolean result = updateById(setmeal);
            }
        }
        return ResultVo.success(null,"修改成功");
    }
}
