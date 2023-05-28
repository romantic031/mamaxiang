package com.jiying2.kaijujiubai.backend_sy.filter;

import cn.hutool.core.bean.BeanUtil;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.EmployeeDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.UserDto;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jiying2.kaijujiubai.backend_sy.constants.RedisConstants.*;

/**
 * 拦截所有请求
 */
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     *
     * 作用：刷新登录用户token实现，避免访问非登录接口，而token失效
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("authorization");

        String key = LOGIN_USER_KEY + token;
        String key1 = LOGIN_EMPLOYEE_KEY + token;
        // 从Redis查询数据
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        Map<Object, Object> employeeMap = stringRedisTemplate.opsForHash().entries(key1);

        // 数据存在
        if (!userMap.isEmpty()) {
            // 将map转换成Bean
            UserDto userDto = BeanUtil.fillBeanWithMap(userMap, new UserDto(), false);

            // 将用户信息保存
            UserHolder.saveUser(userDto);

            // 刷新token有效期
            stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        }

        if (!employeeMap.isEmpty()) {
            // 将map转换成Bean
            EmployeeDto employeeDto = BeanUtil.fillBeanWithMap(employeeMap, new EmployeeDto(), false);

            // 将用户信息保存
            UserHolder.saveEmployee(employeeDto);

            // 刷新token有效期
            stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        }

        // 放行
        return true;
    }
}
