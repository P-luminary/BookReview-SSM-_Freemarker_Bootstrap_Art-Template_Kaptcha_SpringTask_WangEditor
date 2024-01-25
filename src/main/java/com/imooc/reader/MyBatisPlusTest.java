package com.imooc.reader;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Test;
import org.junit.runner.RunWith;
import com.imooc.reader.mapper.TestMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

//23.利用插件调用增删改查  24以Bootstrap开发前端index.ftl(60行)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MyBatisPlusTest {
    @Resource
    private TestMapper testMapper;

    //增
    @org.junit.Test //直接进行调用 与注解重名 故此加包名
    public void testInsert() {
        Test test = new Test();
        test.setContent("MyBatis Plus测试");
        //在接口中 com/imooc/reader/mapper/TestMapper.java
        testMapper.insert(test);
    }

    //改
    @org.junit.Test
    public void testUpdate() {
        Test test = testMapper.selectById(30);//按id号查询
        test.setContent("MyBatis Plus测试1");
        testMapper.updateById(test);
    }

    //删
    @org.junit.Test
    public void testDelete() {
        testMapper.deleteById(30);
    }

    //查
    @org.junit.Test
    public void testSelect() {
        //传入特殊包装对象
        QueryWrapper<Test> queryWrapper = new QueryWrapper<Test>();
//        queryWrapper.eq("id", 31); //eq是等值比较 若写多行语句则sql语言用and连接
        queryWrapper.gt("id", 5);//选择范围 查询大于5的数据
        List<Test> list = testMapper.selectList(queryWrapper);//返回获取7号数据的集合
        System.out.println(list.get(0));
    }
}
