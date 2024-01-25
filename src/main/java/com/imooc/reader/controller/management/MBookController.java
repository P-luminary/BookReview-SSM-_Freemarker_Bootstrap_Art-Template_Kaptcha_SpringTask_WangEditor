package com.imooc.reader.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.reader.entity.Book;
import com.imooc.reader.service.BookService;
import com.imooc.reader.service.exception.BussinessException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//http://localhost/management/book/index.html
//91.后台管理系统 编写富文本编辑器的图片上传功能book.ftl已全实现
//editor.customConfig.uploadImgServer = '/management/book/upload';//设置图片上传地址
//92提供文件上传地址的接口 pom.xml增加文件上传依赖 apache
@Controller
@RequestMapping("/management/book")
public class MBookController {
    @Resource
    private BookService bookService;

    @GetMapping("/index.html")
    public ModelAndView showBook() {
        return new ModelAndView("/management/book");
    }

    /**
     * wangEditor文件上传
     *
     * @param file    上传文件
     * @param request 原生请求对象
     * @return
     * @throws IOException
     */
    //93. 接收提交的文件 上传的文件保存到upload目录中 代码编写
    @PostMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) throws IOException {
        //得到上传目录
        String uploadPath = request.getServletContext().getResource("/").getPath() + "/upload/";//在out里 运行时执行获取路径
        //文件名
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        //原始文件扩展名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));//最后一次获得点
        //保存文件到upload目录
        file.transferTo(new File(uploadPath + fileName + suffix));
        Map result = new HashMap();
        result.put("errno", 0);
        result.put("data", new String[]{"/upload/" + fileName + suffix});
        return result;
    }

    //95.获取书籍数据 分页查询代码再BookService的paging  96下面
    @PostMapping("/create")
    @ResponseBody
    public Map createBook(Book book) {
        Map result = new HashMap();
        try {
            book.setEvaluationQuantity(0);
            book.setEvaluationScore(0f);
            Document doc = Jsoup.parse(book.getDescription()); //一个个片段被解析
            Element img = doc.select("img").first();//选中所有标签提取第一个 获取图书详情第一图的元素对象
            String cover = img.attr("src");//获取当前元素指定值
            book.setCover(cover); //95.来自于description描述的第一幅图
            //用html解析器jsoup.jorg从前台的描述里截取图片位置 96加入依赖
            bookService.createBook(book);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }

    //96.设计列表显示加载 97回到book.ftl
    @GetMapping("/list")
    @ResponseBody
    public Map list(Integer page, Integer limit) {
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        IPage<Book> pageObject = bookService.paging(null, null, page, limit);//返回paging的分页对象
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        //layUI返回值必须要求写的
        result.put("data", pageObject.getRecords()); //当前页面数据
        result.put("count", pageObject.getTotal()); //未分页时记录总数
        return result;
    }

    //98 前台ajax发送的格式保持一致 /managemet/book/id/ + bookId
    @GetMapping("/id/{id}")
    @ResponseBody
    public Map selectById(@PathVariable("id") Long bookId) {
        Book book = bookService.selectById(bookId);
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("data", book); //服务器返回json.data book.ftl132行
        return result;
        //99 点击修改后的确认提交 增添更新操作 BookService.java
    }

    /**
     * 更新图书数据
     *
     * @param book
     * @return
     */
    //100. 更新代码 不要轻易对Book book数据直接更新 根据传入的book编号把数据库记录查出来
    //101 删除方法BookService
    @PostMapping("/update")
    @ResponseBody
    public Map updateBook(Book book) {
        Map result = new HashMap();
        try {
            Book rawBook = bookService.selectById(book.getBookId());
            //通过前台数据获取
            rawBook.setBookName(book.getBookName());
            rawBook.setSubTitle(book.getSubTitle());
            rawBook.setAuthor(book.getAuthor());
            rawBook.setCategoryId(book.getCategoryId());
            rawBook.setDescription(book.getDescription());
            Document doc = Jsoup.parse(book.getDescription());
            String cover = doc.select("img").first().attr("src");
            rawBook.setCover(cover);
            bookService.updateBook(rawBook);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code", ex.getCode()); ///成功时
            result.put("msg", ex.getMsg());//失败时
        }
        return result;
    }

    //102. 封装 以及 code输出  103加入后台首页=>index.ftl 后增加ManagementController
    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map deleteBook(@PathVariable("id") Long bookId) {
        Map result = new HashMap();
        try {
            bookService.deleteBook(bookId);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code", ex.getCode()); ///成功时
            result.put("msg", ex.getMsg());//失败时
        }
        return result;
    }
}
