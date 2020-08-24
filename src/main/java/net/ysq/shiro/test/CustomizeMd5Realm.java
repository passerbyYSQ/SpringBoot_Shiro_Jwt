package net.ysq.shiro.test;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 自定义 Realm，加入Md5Hash
 *
 * 将 认证 或 授权 的数据来源从 .ini 转到 数据库
 *
 * @author passerbyYSQ
 * @create 2020-08-20 21:05
 */
public class CustomizeMd5Realm extends AuthorizingRealm {


    /**
     * Authorization 授权
     * 该方法可能会被调用多次，如果每次都去查数据库，性能较低。应该考虑缓存
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 拿到主体的身份信息（username）
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();

        System.out.println("---");

        // 根据身份信息，从数据库查询其角色信息（权限信息）
        // 假设：ysq 是 admin, user
        if ("ysq".equals(primaryPrincipal)) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            authorizationInfo.addRole("admin");
            authorizationInfo.addRole("user");

            //authorizationInfo.addStringPermission("user:*:01");

            return authorizationInfo;
        }

        return null;
    }

    /**
     * Authentication 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 在 token 种获取 username（身份信息）
        // principal 主角
        String principal = (String) authenticationToken.getPrincipal();

        System.out.println(principal);

        // 根据身份信息，通过 jdbc 从数据查询出 password
        if ("ysq".equals(principal)) {

            // credential: 凭据、凭证（正确的，从数据库读取出来的）
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    principal, "d6206916641be56dc7eb24e04e3463a5",
                    ByteSource.Util.bytes("x0~*Y"), // 注册时（数据库中存储）的随机盐
                    this.getName()
            );
            return authenticationInfo;
        }

        return null;
    }
}
