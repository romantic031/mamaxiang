package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.domain.Category;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.mapper.CategoryMapper;
import com.jiying2.kaijujiubai.backend_sy.service.CategoryService;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public ResultVo listCategory(Category category) {
        //添加条件和排序条件
        LambdaQueryWrapper<Category> lqw=new LambdaQueryWrapper<>();
        lqw.eq(category.getType()!=null,Category::getType,category.getType());
        lqw.orderByAsc(Category::getSort);
        List<Category> list = list(lqw);
//        QueryChainWrapper<Category> categoryQueryChainWrapper = query().eq("type", category.getType()).orderByAsc("sort");
//        List<Category> list = list(categoryQueryChainWrapper);
        return !list.isEmpty() ? ResultVo.success(list) : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"返回分类失败");
    }

    @Override
    public ResultVo saveCategory(Category category) {
        if (StringUtils.isEmpty(UserHolder.getEmployee())){
            return ResultVo.fail(ErrorCode.NO_LOGIN.getCode(),"请登录");
        }
        Category catByName = query().eq("name", category.getName()).one();
        if (!StringUtils.isEmpty(catByName)){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"该分类名称已存在！！！");
        }
        boolean result = save(category);
        return result ? ResultVo.success(null,"添加分类成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"添加分类失败");
    }

    @Override
    public ResultVo updateCategoryById(Category category) {
        boolean result = updateById(category);
        return result ? ResultVo.success(null,"修改成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"修改失败");
    }

    @Override
    public ResultVo removeCategoryById(Long id) {
        if (id==null){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"发生错误了");
        }
        boolean result = removeById(id);
        return result ? ResultVo.success(null,"删除成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"删除失败");
    }

    @Override
    public ResultVo pageByName(PageDto pageDto) {
        Page<Category> pageInfo=new Page<>(pageDto.getPage(),pageDto.getPageSize());
        LambdaQueryWrapper<Category> lqw=new LambdaQueryWrapper<>();
        lqw.like(StrUtil.isNotEmpty(pageDto.getName()),Category::getName,pageDto.getName());
        page(pageInfo,lqw);
        return ResultVo.success(pageInfo);
    }
}
