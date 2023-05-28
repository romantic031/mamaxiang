package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import lombok.Data;

@Data
public class EmployeeDto {
    private Long id;
    private String account;
    private String name;
    private String phone;
    private String gender;
    private String avatar;
    private Integer identity;
}
