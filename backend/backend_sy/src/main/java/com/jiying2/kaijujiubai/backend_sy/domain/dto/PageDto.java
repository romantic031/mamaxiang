package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import lombok.Data;

@Data
/**
 *分页参数
 */
public class PageDto {
    private int page=1;
    private int pageSize=10;
    private String name;
}
