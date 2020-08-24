package net.ysq.shiro.test;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 自定义 Realm
 *
 * 将 认证 或 授权 的数据来源从 .ini 转到 数据库
 *
 * @author passerbyYSQ
 * @create 2020-08-20 21:05
 */
public class CustomizeRealm extends AuthorizingRealm {


    /**
     * Authorization 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
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
        if ("ysq".equalsIgnoreCase(principal)) {

            // credential: 凭据、凭证（正确的，从数据库读取出来的）
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                    principal, "123", this.getName()
            );
            return authenticationInfo;
        }

        return null;
    }
}
