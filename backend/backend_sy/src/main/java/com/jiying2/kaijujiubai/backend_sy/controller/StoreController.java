package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.Store;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/store")
@Api(tags = "店铺接口")
public class StoreController {
    @Resource
    private StoreService storeService;

    @PostMapping("/addStore")
    @ApiOperation("添加店铺")
    public ResultVo addStore(@RequestBody Store store){
        return storeService.addStore(store);
    }

    @GetMapping("/showAllStore")
    @ApiOperation("展示所有店铺")
    public ResultVo showAllStore(String name){
        return storeService.showAllStore(name);
    }

    @GetMapping("/showStoreById/{id}")
    @ApiOperation("展示指定店铺信息")
    public ResultVo showStoreById(@PathVariable("id") Long id){
        return storeService.showStoreById(id);
    }

    @PutMapping("/updateStoreInfo")
    @ApiOperation("更改店铺信息")
    public ResultVo updateStoreInfo(@RequestBody Store store){
        return storeService.updateStoreInfo(store);
    }

}
