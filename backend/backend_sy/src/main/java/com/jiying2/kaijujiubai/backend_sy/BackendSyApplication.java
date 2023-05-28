package com.jiying2.kaijujiubai.backend_sy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.jiying2.kaijujiubai.backend_sy.mapper"})
public class BackendSyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendSyApplication.class, args);
    }

}
