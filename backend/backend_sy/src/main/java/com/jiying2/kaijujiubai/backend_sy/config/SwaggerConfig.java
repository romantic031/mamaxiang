package com.jiying2.kaijujiubai.backend_sy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        ApiInfo apiInfoBuilder = new ApiInfoBuilder()
                .contact(
                        new Contact("qqq","localhost:8081/api/swagger-ui.html","757654410@qq.com")
                )
                .title("mamaxiang_sy接口说明文档")
                .description("20级计算机应用2班开局就躺毕业设计——mamaxiang_sy接口说明文档")
                .version("1.0")
                .build();
        docket.apiInfo(apiInfoBuilder);
        return docket;
    }
}
