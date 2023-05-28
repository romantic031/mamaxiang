package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.Category;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/category")
@Api(tags = "分类接口")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 前台返回分类  也可以后台 传入参数 修改菜品或套餐时显示
     * @param category
     * @return  List<Category>
     */
    @GetMapping("/list")
    public ResultVo listCategory(Category category){
        return categoryService.listCategory(category);
    }

    /**
     * 后台 分页显示分类信息
     * @param pageDto
     * @return
     */
    @GetMapping("/pageByName")
    public  ResultVo pageByName( PageDto pageDto){
        return categoryService.pageByName(pageDto);
    }

    /**
     * 后台 新增分类
     * @param category
     * @return
     */
    @PostMapping("/save")
    private ResultVo save(@RequestBody Category category){
        return categoryService.saveCategory(category);
    }

    /**
     * 后台 修改分类
     * @param category
     * @return
     */
    @PutMapping("/update")
    private ResultVo update(@RequestBody Category category){
        return categoryService.updateCategoryById(category);
    }

    /**
     * 后台 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    private ResultVo delete(@PathVariable("id") Long id){
        return categoryService.removeCategoryById(id);
    }
}
