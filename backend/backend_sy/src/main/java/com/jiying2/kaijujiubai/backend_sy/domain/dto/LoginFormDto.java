package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@AllArgsConstructor
public class LoginFormDto {
    private String account;
    private String password;
    private String phone;
    private String code;
}
