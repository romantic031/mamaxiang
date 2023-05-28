package com.jiying2.kaijujiubai.backend_sy.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.User;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.LoginFormDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.PwdDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService extends IService<User> {
    //注册
    ResultVo register(User user);

    //发送验证码
    ResultVo sendCode(String phone);

    //登录，可以通过账号密码登录，也可以直接手机号验证码登录(不用注册)
    ResultVo login(LoginFormDto loginFormDto);

    //登出
    ResultVo logout(String token);

    //手机号验证码登录未设置密码，设置密码
    ResultVo setPassword(PwdDto PwdDto);



    ResultVo isPassword();

    ResultVo updateIcon(String avatar);

    ResultVo updateAccount(String account);

    ResultVo updateNickName(String nickname);
}
