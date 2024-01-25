package com.imooc.reader.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.Book;
//图书服务
public interface BookService {
    //34.泛型<Book> 说明查询出来的每一行数据都是一个Book对象
    // 瞄准BookService快速生成实现类Alt+Enter => Implement interface
    //35编写com.imooc.reader.service.impl.BookServiceImpl

    /**
     * 分页查询图书
     * @param categoryId 分类编号
     * @param order 排序方式
     * @param page 页号
     * @param rows 每页记录数
     * @return 分页对象
     */
//    47增加两个变量且在BookServiceImpl中也对应增加上
    public IPage<Book> paging(Long categoryId, String order, Integer page, Integer rows);
    /**
     * 根据图书编号查询图书对象
     * @param bookId 图书编号
     * @return 图书对象
     */
    //51.增加一个查询书籍的接口 再去BookServiceImpl中实现
    public Book selectById(Long bookId);

    /**
     * 更新图书评分/评价数量
     */
    //89. 定义完去impl定义实现类
    public void updateEvaluation();

    //95.创建新的图书 BookServiceImpl
    public Book createBook(Book book);

    /**
     * 更新图书
     * @param book 新图书数据
     * @return 更新后的数据
     */
    //99.创建新的更新 BookServiceImpl
    public Book updateBook(Book book);

    //101.删除方法 BookServiceImpl
    public void deleteBook(Long book);
}
