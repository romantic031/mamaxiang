package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.service.UserVipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@CrossOrigin
@RequestMapping("/userVip")
@Api(tags = "用户会员接口")
public class UserVipController {
    @Resource
    UserVipService userVipService;

    @ApiOperation("查询会员相关信息")
    @GetMapping("/getVipInfo")
    public ResultVo getVipInfo(){
        return userVipService.getVipInfo();
    }

    @ApiOperation("加入、续费会员")
    @PutMapping("/becomeVip")
    public ResultVo becomeVip(@RequestParam("day") int day){
        return userVipService.becomeVip(day);
    }

    @ApiOperation("充值金额")
    @PutMapping("/rechargeAmount")
    public ResultVo rechargeAmount(@RequestParam("vipAmount")BigDecimal vipAmount){
        return userVipService.rechargeAmount(vipAmount);
    }
}
