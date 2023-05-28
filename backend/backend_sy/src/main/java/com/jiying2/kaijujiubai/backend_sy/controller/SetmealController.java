package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.Setmeal;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PageDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.SetmealDto;
import com.jiying2.kaijujiubai.backend_sy.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.getImage;
import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.saveFile;

@RestController
@CrossOrigin
@RequestMapping("/setmeal")
@Api(tags = "套餐接口")
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    /**
     * 前台 获取当前分类的套餐
     */
    @GetMapping("/listByCategory/{id}")
    @ApiOperation("获取当前分类的套餐")
    public ResultVo listByCategory(@PathVariable("id") Long cid){
        return setmealService.listByCategory(cid);
    }


     //后台 套餐分页显示 可根据名字查询
    @GetMapping("/pageByName")
    @ApiOperation("后台 套餐分页显示 可根据名字查询")
    public ResultVo pageByname(PageDto pageDto){
        return setmealService.pageByName(pageDto);
    }

    //后台 新增套餐
    @PostMapping("/save")
    @ApiOperation("后台 新增套餐")
    public ResultVo saveSetmeal(@RequestBody SetmealDto setmealDto){
        return setmealService.saveSetmeal(setmealDto);
    }

    //后台 修改套餐的回显
    @GetMapping("/{id}")
    @ApiOperation("后台 修改套餐的回显")
    public ResultVo getByIdWithDish(@PathVariable Long id){
        return setmealService.getByIdWithDish(id);
    }

    //后台 修改套餐
    @PutMapping("/updateSetmeal")
    @ApiOperation("后台 修改套餐")
    public ResultVo update(@RequestBody SetmealDto setmealDto){
        return setmealService.updateWithDish(setmealDto);
    }

    //后台 修改套餐状态
    @PutMapping("/status/{status}")
    @ApiOperation("后台 修改套餐状态")
    public ResultVo updateStatus(@PathVariable int status,@RequestParam List<Long> ids){
        //其中传入的status是反值
        return setmealService.updateStatus(status,ids);
    }

    //后台 删除套餐
    @DeleteMapping("/deleteSetmeal")
    @ApiOperation("后台 删除套餐")
    public ResultVo delete(@RequestParam List<Long> ids){
        return setmealService.removeWithDish(ids);
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
        return saveFile(file, "/setmeal/images");
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
