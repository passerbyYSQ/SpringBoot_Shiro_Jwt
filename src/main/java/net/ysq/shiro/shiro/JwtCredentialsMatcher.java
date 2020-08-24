package net.ysq.shiro.shiro;

import net.ysq.shiro.entity.User;
import net.ysq.shiro.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @author passerbyYSQ
 * @create 2020-08-23 18:42
 */
public class JwtCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        User user = (User) info.getPrincipals().getPrimaryPrincipal();
        String secret = (String) info.getCredentials();

//        String tokenStr = ((JwtToken) token).getToken();
        String tokenStr = (String) token.getPrincipal();

        return JwtUtil.verifyJwt(tokenStr, user.getUsername(), secret);
    }
}
