package com.jiying2.kaijujiubai.backend_sy.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static com.jiying2.kaijujiubai.backend_sy.constants.SystemConstants.UPLOAD_IMAGE_PATH;

public class UploadUtils {
    /**
     * 保存文件
     * @param file 上传文件
     * @param savePath 保存路径
     * @return ResultVo
     */
    public static ResultVo saveFile(MultipartFile file, String savePath) {
        try {
            // 判断文件是否为空
            if (file.isEmpty()) {
                return ResultVo.fail(ErrorCode.FILE_PROBLEM.getCode(),"文件未上传！");
            }

            // 获取文件原始名称以及后缀名
            String originalFilename = file.getOriginalFilename();
            String fileName = createNewFileName(originalFilename, savePath);

            // 保存文件
            file.transferTo(new File(UPLOAD_IMAGE_PATH, fileName));
            // 返回文件保存路径
            return ResultVo.success(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            // 失败，返回错误信息
            return ResultVo.fail(ErrorCode.FILE_PROBLEM.getCode(),"文件上传失败！");
        }
    }

    /**
     * 删除文件
     * @param fileName 文件名称
     * @return ResultVo
     */
    public static ResultVo deleteFile(String fileName) {
        File file = new File(UPLOAD_IMAGE_PATH, fileName);
        if (file.isDirectory()) {
            return ResultVo.fail(ErrorCode.FILE_PROBLEM.getCode(),"错误的文件名称!");
        }
        FileUtil.del(file);
        return ResultVo.success();
    }

    /**
     * 获取图片
     * @param name 文件名称
     * @param response 响应头
     */
    public static void getImage(String name, HttpServletResponse response) {
        if (StrUtil.isBlank(name)) {
            return;
        }
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream=new FileInputStream(new File(UPLOAD_IMAGE_PATH+name));

            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream=response.getOutputStream();

            //设置响应文件类型
            response.setContentType("image/png");

            int len=0;
            byte[] bytes=new byte[1024];
            //  =-1就是没读完，会继续读下去
            while((len=fileInputStream.read(bytes))!=-1){  //把输入流的数据写入到bytes一直到写完
                outputStream.write(bytes,0,len);
                outputStream.flush(); //flush刷新
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String createNewFileName(String originalFilename, String savePath) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
//        int hash = name.hashCode();
//        int d1 = hash & 0xF;
//        int d2 = (hash >> 4) & 0xF;
        // 判断目录是否存在
        File dir = new File(UPLOAD_IMAGE_PATH+name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return StrUtil.format(savePath +"/{}.{}",  name, suffix);
    }
}
