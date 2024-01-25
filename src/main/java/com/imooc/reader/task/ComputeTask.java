package com.imooc.reader.task;

import com.imooc.reader.service.BookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//90.组件注解 不确定是哪个类 但是会被扫描实例化和管理 91引入resources/wangEditor.min.js 编写test.ftl
@Component
public class ComputeTask {
    @Resource
    private BookService bookService;
//  每分钟0秒时候 执行一次Cron表达式
    @Scheduled(cron = "0 * * * * ?")
    public void updateEvaluation(){
        bookService.updateEvaluation();
        System.out.println("已更新所有图书评分");
    }
}
