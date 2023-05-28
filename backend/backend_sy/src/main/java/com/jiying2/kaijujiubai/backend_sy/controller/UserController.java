package com.jiying2.kaijujiubai.backend_sy.controller;

import com.jiying2.kaijujiubai.backend_sy.domain.User;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.LoginFormDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PwdDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.UserDto;
import com.jiying2.kaijujiubai.backend_sy.service.UserService;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.query;
import static com.baomidou.mybatisplus.core.toolkit.Wrappers.update;
import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.getImage;
import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.saveFile;

@RestController
@CrossOrigin
@RequestMapping("/users")
@Api(tags = "用户接口")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public ResultVo register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/sendCode")
    @ApiOperation("手机号 登录发送验证码")
    public ResultVo sendCode(@RequestParam("phone") String phone){
        return userService.sendCode(phone);
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResultVo login(@RequestBody LoginFormDto loginFormDto){
        //判断是手机号登录还是账号登录
        return userService.login(loginFormDto);
    }

    @DeleteMapping("/logout")
    @ApiOperation("用户登出")
    public ResultVo logout(@RequestHeader String token){
        return userService.logout(token);
    }

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public ResultVo userInfo(){
        UserDto user = UserHolder.getUser();
        System.out.println(user);
        return ResultVo.success(user);
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
        return saveFile(file, "/user/avatar");
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

    @PutMapping("/updateIcon")
    @ApiOperation(value = "修改用户头像")
    public ResultVo updateIcon(@RequestBody UserDto user) {
        return userService.updateIcon(user.getAvatar());
    }

    @PutMapping("isPassword")
    @ApiOperation(value = "查询是否设置密码")
    public ResultVo isPassword() {
        return userService.isPassword();
    }

    @PutMapping("/setPassword")
    @ApiOperation(value = "设置/修改密码", notes = "当新用户未设置密码，则新密码(newPassword)作为密码；当用户已经设置密码，则需要旧密码(oldPassword)和新密码(newPassword)才能修改密码")
    public ResultVo setPassword(@RequestBody PwdDto pwdDto) {
        return userService.setPassword(pwdDto);
    }

    @PutMapping("/updateAccount")
    @ApiOperation(value = "更改账号")
    public ResultVo updateAccount(@RequestBody UserDto userDto){
        return userService.updateAccount(userDto.getAccount());
    }

    @PutMapping("/updateNickName")
    @ApiOperation(value = "更改昵称")
    public ResultVo updateNickName(@RequestBody UserDto userDto){
        return userService.updateNickName(userDto.getNickname());
    }
}
