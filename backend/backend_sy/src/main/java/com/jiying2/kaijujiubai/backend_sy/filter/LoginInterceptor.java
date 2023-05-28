package com.jiying2.kaijujiubai.backend_sy.filter;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.jiying2.kaijujiubai.backend_sy.domain.dto.EmployeeDto;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.UserDto;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 非登录拦截
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDto user = UserHolder.getUser();
        EmployeeDto employee = UserHolder.getEmployee();
        if (user == null && employee==null) {
            falseResult(response,"无效请求，请登录");
            return false;
        }
        return true;
    }


    public void falseResult(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(301);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(ResultVo.fail(ErrorCode.NO_LOGIN.getCode(),message)));
        return;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
        UserHolder.removeEmployee();
    }
}
