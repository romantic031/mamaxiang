package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.Dish;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.DishDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.getImage;
import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.saveFile;

@RestController
@CrossOrigin
@RequestMapping("/dish")
@Api(tags = "菜品接口")
public class DishController {
    @Resource
    private DishService dishService;

    /**
     * 前台 获取当前分类的菜品
     * @param dish
     * @return  d
     */
//    @GetMapping("/listByCategory/{id}")
    @GetMapping("/listByCategory")
    public ResultVo listByCategory(Dish dish){
        return dishService.listByCategory(dish);
    }

    /**
     * 后台 菜品分页显示 可根据名字查询 不显示口味 修改时才显示
     * @param pageDto
     * @return
     */
    @GetMapping("/pageByName")
    public ResultVo pageByName(PageDto pageDto){
        return dishService.pageByName(pageDto);
    }

    /**
     * 后台 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping("/save")
    //DishDto中包括口味
    public ResultVo saveDish(@RequestBody DishDto dishDto){
        return dishService.saveWithFlavor(dishDto);
    }

    /**
     * 后台 修改菜品 菜品的回显
     * 根据dishId查询菜品信息和口味信息，
     * 需要同时操作两个表
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResultVo getDish(@PathVariable("id") Long id){
        return dishService.getByIdWithFlavor(id);
    }

    /**
     * 后台 修改菜品
     * 需要同时修改两个表
     * @param dishDto
     * @return
     */
    @PutMapping("/updateDish")
    public ResultVo updateDish(@RequestBody DishDto dishDto){
        return  dishService.updateWithFlavor(dishDto);
    }

    /**
     * 修改菜品状态，启售或停售
     * @param status
     * @param ids
     * @return
     */
    @PutMapping("/status/{status}")
    public ResultVo updateStatus(@PathVariable int status,@RequestParam List<Long> ids){
        //其中传入的status是反值
        return dishService.updateStatus(status,ids);
    }

    @DeleteMapping("/deleteDish")
    public ResultVo deleteDish(@RequestParam List<Long> ids){
        return dishService.deleteDish(ids);
    }

    /**
     * 文件上传 ,要先登录才能上传要不然会被拦截
     *
     * @param file file不能修改
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传图片" , notes = "上传成功，返回头像存放路径")
    public ResultVo upload(@RequestPart("file") MultipartFile file) {
        return saveFile(file, "/dish/images");
    }
    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    //图片通过输出流下载回页面，而输出流需要response获得
    public void download(String name, HttpServletResponse response) {
        getImage(name, response);
    }
}

