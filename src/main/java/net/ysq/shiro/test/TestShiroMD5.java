package net.ysq.shiro.test;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author passerbyYSQ
 * @create 2020-08-20 21:20
 */
public class TestShiroMD5 {
    public static void main(String[] args) {

        // 创建一个 Md5Hash 算法
        // 默认把 salt 拼接到 source字符串前面，再进行 MD5 解密，然后 Hash散列
        // 第3个参数：散列次数

        //202cb962ac59075b964b07152d234b70
        //Md5Hash md5Hash = new Md5Hash("123");

        //1d82c745ecf9ea1641f37cdcf93877a0
        //Md5Hash md5Hash = new Md5Hash("123", "x0~*Y");

        //d6206916641be56dc7eb24e04e3463a5
        Md5Hash md5Hash = new Md5Hash("123", "x0~*Y", 1024);

        // 错误用法
//        md5Hash.setBytes(.getBytes());

        // 转成16进制
        String s = md5Hash.toHex();

        System.out.println(s);

    }
}
