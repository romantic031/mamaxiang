//package com.jiying2.kaijujiubai.backend_sy.config;
//
//import com.qwj.elemei.common.JacksonObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//import java.util.List;
//
//@Slf4j
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurationSupport {
//    /**
//     * 设置静态资源映射
//     * @param registry
//     */
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        log.info("开始静态资源映射");
//        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
//        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
////        registry.addResourceHandler("/about/**").addResourceLocations("file:F:/javaee/elemei_take_out/elemei_take_out/src/main/resources/backend/page/about/");
////        registry.addResourceHandler("/about/**").addResourceLocations("file:F:/javaee/elemei_take_out/elemei_take_out/src/main/resources/front/page/about/");
//
//    }
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){
//        log.info("扩展消息转换器");
//        //创建消息转换器对象,用于将数据转换成json格式，原本就有默认转换器
//        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
//        //设置对象转换器，底层使用Jackson(创建的类)将java对象转换为json
//        messageConverter.setObjectMapper(new JacksonObjectMapper());
//        //将上面的消息转换器对象追加到mvc框架的转换器集合中
//        converters.add(0,messageConverter);
//    }
//
//}
