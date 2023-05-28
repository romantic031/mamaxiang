package com.jiying2.kaijujiubai.backend_sy.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static com.jiying2.kaijujiubai.backend_sy.utils.UploadUtils.getImage;


@RestController
@CrossOrigin
@RequestMapping("/resources")

public class ResourceController {
    @GetMapping("/image")
    //图片通过输出流下载回页面，而输出流需要response获得
    public void download(String name, HttpServletResponse response) {
        getImage(name, response);
    }
}



