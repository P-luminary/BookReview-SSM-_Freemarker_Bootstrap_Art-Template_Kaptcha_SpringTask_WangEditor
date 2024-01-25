package com.imooc.reader.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
//62.完成图片的随机生成与控制台的输出相同 63将原型里的 注册页 复制到项目工程register.ftl
@Controller
public class KaptchaController {
    @Resource
    private Producer kaptchaProducer;
//    Ioc动态注入 因为验证码组件设计的时候没有考虑过SpringMVC的集成 要使用原生的请求与响应
    @GetMapping("/verify_code")
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //响应立即过期
        response.setDateHeader("Expires",0);
        //不存储 不缓存任何图片数据
        response.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
        response.setHeader("Cache-Control","post-check=0,pre-check=0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/png");
        //生成验证码字符文本
        String verifyCode = kaptchaProducer.createText();
        request.getSession().setAttribute("kaptchaVerifyCode",verifyCode);
        System.out.println( request.getSession().getAttribute("kaptchaVerifyCode"));
        BufferedImage image = kaptchaProducer.createImage(verifyCode);//创建验证码图片 二进制图片
        //二进制用getOutputStream() 字符用getWritter()
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image,"png",out);//输出图片流
        out.flush(); //立即输出
        out.close(); //关闭输出流

    }
}
