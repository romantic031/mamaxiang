package com.jiying2.kaijujiubai.backend_sy.utils;

import com.jiying2.kaijujiubai.backend_sy.domain.dto.EmployeeDto;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.UserDto;

public class UserHolder {
    private static final ThreadLocal<UserDto> threadLocal = new ThreadLocal<>();
    private static final ThreadLocal<EmployeeDto> threadLocal1 = new ThreadLocal<>();

    public static void saveUser(UserDto userDto) {
        threadLocal.set(userDto);
    }
    public static void saveEmployee(EmployeeDto employeeDto) {
        threadLocal1.set(employeeDto);
    }

    public static UserDto getUser() {
        return threadLocal.get();
    }

    public static void removeUser() {
        threadLocal.remove();
    }
    public static EmployeeDto getEmployee() {
        return threadLocal1.get();
    }

    public static void removeEmployee() {
        threadLocal1.remove();
    }
}
