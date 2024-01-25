package com.imooc.reader.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.reader.entity.Test;
//22.为了和BaseMapper里的insert()区别开 这个改名成insertSample
//接口作用Mybatis通过接口自动生成实现类 23.MyBatisPlusTest.java
public interface TestMapper extends BaseMapper<Test> {
// 系统中接口很多不是所有的都对应mapper 9.application.xml
    public void insertSample();
}
