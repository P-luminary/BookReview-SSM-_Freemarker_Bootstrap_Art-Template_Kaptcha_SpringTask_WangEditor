package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.reader.entity.Book;
import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.MemberReadState;
import com.imooc.reader.mapper.BookMapper;
import com.imooc.reader.mapper.EvaluationMapper;
import com.imooc.reader.mapper.MemberReadStateMapper;
import com.imooc.reader.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

//35. 实现分类代码编写  36生成测试用例 Code generate Test => BookServiceImplTest
@Service("bookService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;

    /**
     * 分页查询图书
     *
     * @param categoryId 分类编号
     * @param order      排序方式
     * @param page       页号
     * @param rows       每页记录数
     * @return 分页对象
     */
    public IPage<Book> paging(Long categoryId, String order, Integer page, Integer rows) {
        Page<Book> p = new Page<Book>(page, rows);
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();
        //47.编写匹配条件 48去BookController更改调用参数
        if (categoryId != null && categoryId != -1) {//代表传入了有效分类编号
            queryWrapper.eq("category_id", categoryId);//查询的where子句
        }
        if (order != null) {
            if (order.equals("quantity")) { //前台必须传入↓ 评价人数排序
                queryWrapper.orderByDesc("evaluation_quantity");//降序排序
            } else if (order.equals("score")) {
                queryWrapper.orderByDesc("evaluation_score");//评分降序
            }
        }
        IPage<Book> pageObject = bookMapper.selectPage(p, queryWrapper);//传入两个参数: page对象哪一页数据  每页记录数[条件构造器]
        return pageObject;
    }

    //51实现后再向上推进 BookController
    public Book selectById(Long bookId) {
        Book book = bookMapper.selectById(bookId);
        return book;
    }

    /**
     * 更新图书评分/评价数量  Controller直接面向Service Service面向Mapper
     */
    //89. 更新操作需要声明式事务 开启  90applicationContext.xml开启task
    @Transactional
    public void updateEvaluation() {
        bookMapper.updateEvaluation();
    }

    //95.数据增加 MBookController
    @Transactional
    public Book createBook(Book book) {
        bookMapper.insert(book);
        return book;
    }

    /**
     * 更新图书
     * @param book 新图书数据
     * @return 更新后的数据
     */
    //99.更新数据 100 MBookController
    @Transactional
    public Book updateBook(Book book) {
        bookMapper.updateById(book);
        return null;
    }

    //101.删除相关数据 上面增加memberReadStateMapper evaluationMapper
    //一次性三个表删除 有声明式事务不会删一半 102 MBookController.java
    /**
     * 删除图书及其数据
     * @param bookId 图书编号
     */
    @Transactional
    public void deleteBook(Long bookId) {
        bookMapper.deleteById(bookId);//单独删除id效率太低 写个构造器
        QueryWrapper<MemberReadState> mrsQueryWrapper = new QueryWrapper<MemberReadState>();
        mrsQueryWrapper.eq("book_id", bookId);
        memberReadStateMapper.delete(mrsQueryWrapper);
        QueryWrapper<Evaluation> evaluationQueryWrapper = new QueryWrapper<Evaluation>();
        evaluationQueryWrapper.eq("book_id", bookId);
        evaluationMapper.delete(evaluationQueryWrapper);
    }
}
