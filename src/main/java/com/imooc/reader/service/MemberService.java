package com.imooc.reader.service;

import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.Member;
import com.imooc.reader.entity.MemberReadState;

//68.创建一个实现类MemberServiceImpl.java[Alt + Enter]
public interface MemberService {
    /**
     * 会员注册,创建新会员
     *
     * @param username
     * @param password
     * @param nickname
     * @return
     */
    public Member createMember(String username, String password, String nickname);

    //74.检查登录方法声明接口 再去MemberServiceImpl对方法进行实现
    public Member checkLogin(String username, String password);

    //78.获取阅读状态 再去实现这个方法MemberServiceImpl

    /**
     * 获取阅读状态
     *
     * @param memberId 会员编号
     * @param bookId   图书编号
     * @return 阅读状态对象
     */
    public MemberReadState selectMemberReadState(Long memberId, Long bookId);


    /**
     * 更新阅读状态
     * @param memberId 会员编号
     * @param bookId 图书编号
     * @param readState 阅读状态
     * @return 阅读对象状态
     */         //81定义方法 MemberServiceImpl实现状态
    public MemberReadState updateMemberReadState(Long memberId, Long bookId, Integer readState);

    /**
     * 发布新的短评
     * @param memberId 会员编号
     * @param bookId 图书编号
     * @param score 评分
     * @param content 短评内容
     * @return 短评对象
     */   //84.短评代码!  MemberServiceImpl 组一个全新的对象
    public Evaluation evaluate(Long memberId, Long bookId, Integer score, String content);

    /**
     * 短评点赞
     * @param evaluationId 短评编号
     * @return 短评对象
     */
//    87完成点赞核心实现 MemberServiceImpl
    public Evaluation enjoy(Long evaluationId);
}
