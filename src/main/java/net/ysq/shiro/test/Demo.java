package net.ysq.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

/**
 * MD5算法
 *
 * 1、不可逆，一般用作 加密 或 签名
 * 2、如果内容相同，无论执行多少次MD5算法生成结果都是一样的
 *
 * 生成结果：始终是一个16进制的长孺为32位的字符串
 *
 * @author passerbyYSQ
 * @create 2020-08-20 20:13
 */
public class Demo {

    public static void main(String[] args) {

        // ctrl + d 复制当前行
        // ctrl + y 删除当前行
        // ctrl + alt + l 格式化代码

        // 全局的安全管理器
        DefaultSecurityManager securityManager = new DefaultSecurityManager();

        CustomizeMd5Realm realm = new CustomizeMd5Realm();
        // 设置 Realm 使用MD5算法的哈希凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(1024);
        realm.setCredentialsMatcher(credentialsMatcher);

        // 设置 Realm（类似数据源）
//        securityManager.setRealm(new IniRealm("classpath:shiro.ini"));
//        securityManager.setRealm(new CustomizeRealm());
        securityManager.setRealm(realm);

        // 全局的安全工具类，设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);

        // 拿到主体
        Subject subject = SecurityUtils.getSubject();

        // 创建令牌，模拟主体携带令牌访问
        UsernamePasswordToken token = new UsernamePasswordToken("ysq", "123");

        // 调用主体的方法，进行 认证
        // 认证不通过抛出对应异常
        // try - catch 快捷键 ctrl + alt + t
        try {
            // 是否已经认证
            System.out.println(subject.isAuthenticated());

            subject.login(token);

            System.out.println("登录成功: " + subject.isAuthenticated());
        } catch (UnknownAccountException e1) {
            // username 错误，会抛出 UnknownAccountException 异常
            e1.printStackTrace();
            System.out.println("用户名错误");

        } catch (IncorrectCredentialsException e2) {
            // password 错误，会抛出 IncorrectCredentialsException 异常
            // Credential: 凭证
            e2.printStackTrace();
            System.out.println("密码错误");
        }

//        catch (AuthenticationException e) {
//            e.printStackTrace();
//        }

        // 如果主体已经认证
        if (subject.isAuthenticated()) {
            // 基于单角色的权限控制
//            System.out.println(subject.hasRole("admin"));

            // 基于多角色的权限控制
            // 是否【同时】具有多种角色
//            System.out.println(subject.hasAllRoles(Arrays.asList("admin", "user")));

            // 判断是否具有对应角色，返回一个boolean数组
//            boolean[] hasRole = subject.hasRoles(Arrays.asList("admin", "user", "super"));
//            for (boolean b : hasRole) {
//                System.out.println(b);
//            }

            // 基于权限字符串的访问控制
            // 资源标识符:操作:资源类型
            subject.isPermitted("user:update:01");

            // 判断是否具有对应权限

            // 判断是否【同时】具有多个权限
//            subject.isPermittedAll()
        }



    }
}
