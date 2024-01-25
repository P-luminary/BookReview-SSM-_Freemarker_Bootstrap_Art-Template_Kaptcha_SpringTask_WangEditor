package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Category;
import com.imooc.reader.mapper.CategoryMapper;
import com.imooc.reader.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

//28.向接口编程的规则,注入写好的CategoryMapper接口 设置Transactional事务传播
//默认所有方法是不使用事务的 查询较多的方法不使用事务 写入方法较多下写入事务
//29生成测试类 code-generate-Test  com/imooc/reader/service/impl/CategoryServiceImplTest.java
@Service("categoryService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    /**
     * 查询所有图书分类
     * @return 图书分类List
     */
    @Override
    public List<Category> selectAll() {
        //查询列表 返回多个数据 插入条件构造器[查询所有]
        List<Category> list = categoryMapper.selectList(new QueryWrapper<Category>());
        return list;
    }
}
