package net.ysq.shiro.shiro.realm;

import com.sun.javafx.scene.traversal.Algorithm;
import net.ysq.shiro.entity.Permission;
import net.ysq.shiro.entity.Role;
import net.ysq.shiro.entity.User;
import net.ysq.shiro.service.UserService;
import net.ysq.shiro.utils.ApplicationContextUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2020-08-20 23:31
 */
public class LoginRealm extends AuthorizingRealm {

    /*
    如果@Autowired，需要在当前类前加上@Componet注解，将当前类的实例注入到IOC容器
    但是如果有多个类似的类，都要注册到容器中，不太好。我可以新建一个管理类，注册到容器中，
    为我们统一获取@Autowired的实例
     */
//    @Autowired
//    private UserService userService;


    /**
     * 或者在ShiroConfig中设置
     */
    public LoginRealm() {
        // 匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 设置匹配器的加密算法
        hashedCredentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
        // 设置匹配器的散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        // 将对应的匹配器设置到Realm中
        this.setCredentialsMatcher(hashedCredentialsMatcher);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取身份信息
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();

        // 从容器中获取UserService组件
        UserService userService = (UserService) ApplicationContextUtil.getBean("userService");
        List<Role> roles = userService.getRolesByUsername(primaryPrincipal);

        if (!CollectionUtils.isEmpty(roles)) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            for (Role role : roles) {
//                System.out.println("---: " + role.getId() + "----" + role.getRoleName());
                authorizationInfo.addRole(role.getRoleName());

                // 查询当前角色的权限信息
                List<Permission> perms = userService.getPermsByRoleId(role.getId());
                if (!CollectionUtils.isEmpty(perms)) {
                    for (Permission perm : perms) {
                        authorizationInfo.addStringPermission(perm.getPermissionName());
                    }
                }

            }
            return authorizationInfo;
        }

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取身份信息
        String principal = (String) token.getPrincipal();
        // 从容器中获取UserService组件
        UserService userService = (UserService) ApplicationContextUtil.getBean("userService");

        User user = userService.findByUsername(principal);

        if (!ObjectUtils.isEmpty(user)) {
            // 返回正确的信息（数据库存储的），作为比较的基准
            return  new SimpleAuthenticationInfo(
                    user, user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()), this.getName()
            );
        }

        return null;
    }
}
