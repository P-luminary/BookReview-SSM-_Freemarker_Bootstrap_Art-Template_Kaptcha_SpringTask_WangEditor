package com.imooc.reader.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.reader.entity.Category;
//25.BaseMapper自动提供了增删改查的功能 泛型指向Category
// 图书分类Mapper接口 26在mappers中创建一个category.xml
public interface CategoryMapper extends BaseMapper<Category> {

}
