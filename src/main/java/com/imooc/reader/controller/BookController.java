package com.imooc.reader.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.*;
import com.imooc.reader.service.BookService;
import com.imooc.reader.service.CategoryService;
import com.imooc.reader.service.EvaluationService;
import com.imooc.reader.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

//30.显示之前写好的index.ftl
@Controller
public class BookController {
    //传入模板数据 分类信息
    @Resource
    private CategoryService categoryService;
    @Resource
    private BookService bookService;
    //56.增加一个注入 在下面具体调用的地方查询71行
    @Resource
    private EvaluationService evaluationService;

    @Resource
    private MemberService memberService;
    //showIndex与url绑定  31继续向前推进因为index.ftl所有数据都是静态写死的
    //31要对每一个分类进行读取 List标签<#list>读取 转至index.ftl 64行

    /**
     * 显示首页
     *
     * @return
     */
    @GetMapping("/")
    public ModelAndView showIndex() {
        ModelAndView mav = new ModelAndView("index");
        List<Category> categoryList = categoryService.selectAll();//拿到列表
        //结果放入其中 属性名,值
        mav.addObject("categoryList", categoryList);
        return mav;
    }
//37.编写一个page方法 上面加个@Resource 绑定一个Ajax @GetMapping
    //http://localhost/books 非常长的JSON序列化结果  38index.ftl 40行

    /**
     * 分页查询图书列表
     *
     * @param p 页号
     * @return 分页对象
     */
    //48.更改调用参数 更改BookServiceImplTest.java调用参数 49回到index.ftl 增加function第二个
    @GetMapping("/books")
    @ResponseBody //具体数据来自客户端传入
    public IPage<Book> selectBook(Long categoryId, String order, Integer p) {
        if (p == null) {
            p = 1;
        }
        IPage<Book> pageObject = bookService.paging(categoryId, order, p, 10);
        return pageObject;
    }

    //51.进行页面的绑定 获取读书编号 52把所有ftl相对路径改成绝对路径 ./前面的.删掉
    //加上了./就是  localhost/book/......   删掉就是 localhost/......
    //52更改detail.ftl将静态页面改成动态页面
    @GetMapping("/book/{id}") //↓ 路径变量 和 上面的保持一致
    //79.新增一个参数 获取当前用户登录信息
    public ModelAndView showDetail(@PathVariable("id") Long id, HttpSession session) {
        Book book = bookService.selectById(id);
        //56.写完并放入 mav.addObject 57得到了对象就要去detail.ftl渲染循环遍历[写短评下方的div]
        List<Evaluation> evaluationList = evaluationService.selectByBookId(id);
        //79.将之前设置的用户信息拿出来 两种情况 ①会员没登录就null ②会员登录了member对象存在了 注入memberService
        Member member = (Member)session.getAttribute("loginMember");
        ModelAndView mav = new ModelAndView("/detail");
        if (member != null) { //获取会员阅读状态    会员编号 图书编号  80 detail.ftl想看[1]与看过[2] 页面上方找script块
            MemberReadState memberReadState = memberService.selectMemberReadState(member.getMemberId(), id);
            mav.addObject("memberReadState", memberReadState);

        }
        mav.addObject("book", book);
        mav.addObject("evaluationList", evaluationList);
        return mav;
    }
}
