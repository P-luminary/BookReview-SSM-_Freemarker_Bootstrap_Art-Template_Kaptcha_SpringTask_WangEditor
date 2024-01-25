package com.imooc.reader.service;
//27.有了service的接口 28就要有实现实现类存放包CategoryServiceImpl

import com.imooc.reader.entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> selectAll();
}
