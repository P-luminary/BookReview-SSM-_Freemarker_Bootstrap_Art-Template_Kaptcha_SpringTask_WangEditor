package com.imooc.reader.service;

import com.imooc.reader.mapper.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TestService {
// 13.运行时注入生成的对象 自动生成Test 选中上面的类 Code generate 14TestServiceTest.java
    @Resource
    private TestMapper testMapper;
//      @Transactional 要么全部完成 要么什么也不做
    @Transactional
    public void batchImport(){
        for (int i = 0; i < 5; i++) {
//            if (i==3) {
//                throw new RuntimeException("预期外异常");
//            }
            testMapper.insertSample();
        }
    }
}
