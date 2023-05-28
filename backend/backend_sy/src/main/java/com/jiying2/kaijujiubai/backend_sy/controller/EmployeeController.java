package com.jiying2.kaijujiubai.backend_sy.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiying2.kaijujiubai.backend_sy.domain.Employee;
import com.jiying2.kaijujiubai.backend_sy.domain.User;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.*;
import com.jiying2.kaijujiubai.backend_sy.service.EmployeeService;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jiying2.kaijujiubai.backend_sy.constants.RedisConstants.*;
import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.getImage;
import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.saveFile;

@RestController
@CrossOrigin
@RequestMapping("/employee")
@Api(tags = "员工接口")
public class EmployeeController {
    @Resource
    EmployeeService employeeService;

    /**
     * 登录 可通过手机号验证码和账号密码登录
     * @param loginFormDto
     * @return
     */
    @PostMapping("/login")
    public ResultVo login(@RequestBody LoginFormDto loginFormDto){
        return employeeService.login(loginFormDto);
    }

    /**
     * 手机号登录时发送验证码
     * @param phone
     * @return
     */
    @GetMapping("/sendCode")
    @ApiOperation("手机号 登录发送验证码")
    public ResultVo sendCode(@RequestParam("phone") String phone){
        return employeeService.sendCode(phone);
    }

    @GetMapping("/employeeInfo")
    @ApiOperation("当前登录的员工信息")
    public ResultVo employeeInfo(){
        EmployeeDto employeeDto = UserHolder.getEmployee();
        return ResultVo.success(employeeDto);
//        return employeeService.getDtoById(id);
    }
    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping("/saveEmployee")
    @ApiOperation("后台管理员 新增员工")
    public ResultVo saveEmployee(@RequestBody Employee employee){
        return employeeService.saveNewEmployee(employee);
//        return null;
    }

    @PutMapping("isPassword")
    @ApiOperation(value = "查询是否设置密码")
    public ResultVo isPassword() {
        return employeeService.isPassword();
    }

    @PutMapping("/setPassword")
    @ApiOperation(value = "设置/修改密码", notes = "当新用户未设置密码，则新密码(newPassword)作为密码；当用户已经设置密码，则需要旧密码(oldPassword)和新密码(newPassword)才能修改密码")
    public ResultVo setPassword(@RequestBody PwdDto pwdDto) {
        return employeeService.setPassword(pwdDto);
    }

    /**
     * 管理员修改员工信息
     */
    @PutMapping("/updateInfo")
    @ApiOperation("后台管理员 修改员工信息")
    public ResultVo updateInfo(@RequestBody Employee employee){
        return employeeService.updateInfo(employee);
    }

    /**
     * 管理员修改员工信息的回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("后台管理员 修改员工信息的时候的回显")
    public ResultVo getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }
    /**
     * 后台分页显示
     */
    @GetMapping("/pageByName")
    @ApiOperation("后台 分页显示员工")
    public ResultVo pageByName( PageDto pageDto){
//        return  ResultVo.success(pageInfo);
        return employeeService.pageByName(pageDto);
    }

    @DeleteMapping("/logout")
    @ApiOperation("用户登出")
    public ResultVo logout(@RequestHeader String token){
        return employeeService.logout(token);
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
        return saveFile(file, "/employee/avatar");
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
