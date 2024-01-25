package com.imooc.reader.controller;

import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.Member;
import com.imooc.reader.service.exception.BussinessException;
import com.imooc.reader.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//64.嵌入验证码 65编写单机验证码刷新register.ftl中的function reloadVerifyCode()
@Controller
public class MemberController {
    //72后续工作 进行注入
    @Resource
    private MemberService memberService;

    @GetMapping("/register.html")
    public ModelAndView showRegister() {
        return new ModelAndView("/register");
    }

    @GetMapping("/login.html") //73.显示登录界面  74MemberService.java新增方法声明
    public ModelAndView showLogin() {
        return new ModelAndView("/login");
    }

    //67. 验证码匹配比对 request拿到对象  68编写会员服务MemberService.java
    @PostMapping("/registe")
    @ResponseBody
    public Map registe(String vc, String username, String password, String nickname, HttpServletRequest request) {
        //接收kaptchaController.java中的request.getSession().setAttribute响应
        //正确验证码
        String verityCode = (String) request.getSession().getAttribute("kaptchaVerifyCode");
        //验证码比对  后面的是大小写对比
        Map result = new HashMap();
        if (vc == null || verityCode == null || !vc.equalsIgnoreCase(verityCode)) {
            result.put("code", "VC01");
            result.put("msg", "验证码错误");
        } else {
            //72.调用过程 73将登录页复制进去 74MemberServiceImpl
            try {
                memberService.createMember(username, password, nickname);
                result.put("code", "0");
                result.put("msg", "success");
            } catch (BussinessException ex) {
                ex.printStackTrace();
                result.put("code", ex.getCode());
                result.put("msg", ex.getMsg());
            }
        }
        return result;
    }

    // 75.HttpSession session 登录校验后会将这个会员信息存放到session中
    // 76登录校验成功以后会返回一个member对象 存放在session中
    @PostMapping("/check_login") //前台url
    @ResponseBody
    public Map checkLogin(String username, String password, String vc, HttpSession session) {
        //正确验证码                     ↓为了得到session对象
        String verityCode = (String) session.getAttribute("kaptchaVerifyCode");
        //验证码比对  后面的是大小写对比
        Map result = new HashMap();
        if (vc == null || verityCode == null || !vc.equalsIgnoreCase(verityCode)) {
            result.put("code", "VC01");
            result.put("msg", "验证码错误");
        } else {
            try {//处理成功
                Member member = memberService.checkLogin(username, password);
                //76.member存放在session中  77去index.ftl下方登录块改造
                session.setAttribute("loginMember", member);
                result.put("code", "0");
                result.put("msg", "success");
            } catch (BussinessException ex) { //处理失败
                ex.printStackTrace();
                result.put("code", ex.getCode());
                result.put("msg", ex.getMsg());
            }
        }
        return result;
    }

    //81. 完成更新状态的事务  82去完成detail.ftl后面的loginMember存在的代码
    @PostMapping("/update_read_state")
    @ResponseBody
    public Map updateReadState(Long memberId, Long bookId, Integer readState) {
        Map result = new HashMap();
        try {
            memberService.updateMemberReadState(memberId, bookId, readState);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }

    //85.与web交互功能 前台请求传入数据 86回到detail.ftl写
    @PostMapping("/evaluate")
    @ResponseBody
    public Map evaluate(Long memberId, Long bookId, Integer score, String content) {
        Map result = new HashMap();
        try {
//    Evaluation eva = memberService.evaluate(memberId, bookId, score, content);
            memberService.evaluate(memberId, bookId, score, content);
            result.put("code", "0");
            result.put("msg", "success");
//          result.put("evaluation", eva);
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }

    //87.实现controller 88让客户端页面发送ajax请求detail.ftl
    @PostMapping("/enjoy")
    @ResponseBody
    public Map evaluate(Long evaluationId) {
        Map result = new HashMap();
        try {
//    Evaluation eva = memberService.evaluate(memberId, bookId, score, content);
            Evaluation eva = memberService.enjoy(evaluationId);
            result.put("code", "0");
            result.put("msg", "success");
            result.put("evaluation", eva); //包含最新点赞数
        } catch (BussinessException ex) {
            ex.printStackTrace();
            result.put("code", ex.getCode());
            result.put("msg", ex.getMsg());
        }
        return result;
    }
}
