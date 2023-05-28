package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import lombok.Data;

@Data
public class PwdDto {
    private String phone;
    private String oldPassword;
    private String newPassword;
}
