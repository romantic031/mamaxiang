package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.Employee;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EmployeeService extends IService<Employee> {
    ResultVo login(LoginFormDto loginFormDto);

    ResultVo sendCode(String phone);

//    ResultVo getDtoById(Long id);

    ResultVo saveNewEmployee(Employee employee);

    ResultVo getEmployeeById(Long id);

    ResultVo updateInfo(Employee employee);

    ResultVo pageByName(PageDto pageDto);

    ResultVo isPassword();

    ResultVo setPassword(PwdDto pwdDto);

    ResultVo logout(String token);
}
