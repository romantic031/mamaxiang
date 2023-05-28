package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.Category;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CategoryService extends IService<Category> {
    ResultVo listCategory(Category category);

    ResultVo saveCategory(Category category);

    ResultVo updateCategoryById(Category category);

    ResultVo removeCategoryById(Long id);

    ResultVo pageByName(PageDto pageDto);
}
