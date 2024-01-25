package com.imooc.reader.utils;

import org.apache.commons.codec.digest.DigestUtils;
//70.生成MD5方法 返回MemberServiceImpl编写随机数
public class MD5Utils {
    public static String md5Digest(String source, Integer salt){
        char[] ca = source.toCharArray();//获取字符数组
        for (int i = 0; i < ca.length; i++) {
            ca[i] = (char) (ca[i] + salt);
        }
        String target = new String(ca);
        String md5 = DigestUtils.md5Hex(target);
        return md5;
    }
}
