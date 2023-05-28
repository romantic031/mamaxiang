package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String phone;
    private String gender;
    private String account;
    private String nickname;
    private String avatar;
    private Integer isVip;
}
