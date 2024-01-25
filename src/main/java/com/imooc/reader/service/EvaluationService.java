package com.imooc.reader.service;

import com.imooc.reader.entity.Evaluation;

import java.util.List;
//55.再创建一个它的实现类 Alt+回车 com/imooc/reader/service/impl/EvaluationServiceImpl.java
public interface EvaluationService{
    /**
     * 按图书编号查询有效短评
     * @param bookId 图书编号
     * @return 评论列表
     */
    public List<Evaluation> selectByBookId(Long bookId);
}
