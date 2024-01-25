package com.imooc.reader.controller.management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
//103.后台管理系统控制器 104 修改index.ftl的静态地址
/**
 * 后台管理系统控制器
 */
@Controller
@RequestMapping("/management") //功能启动后台所需
public class ManagementController {
    @GetMapping("/index.html")
    public ModelAndView showIndex(){
        return new ModelAndView("/management/index");
    }
}
