package com.imooc.reader.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

//14. 运行时自动初始IoC容器 + 说明配置文件在什么地方完成初始化 15依赖java servlet pom.xml
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestServiceTest {
    @Resource
    private TestService testService;

    @Test
    public void batchImport() {
        testService.batchImport();
        System.out.println("批量导入成功");
    }
}