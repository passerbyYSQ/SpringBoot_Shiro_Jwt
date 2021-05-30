package net.ysq.shiro.shiro.realm;

import net.ysq.shiro.po.User;
import net.ysq.shiro.service.UserService;
import net.ysq.shiro.shiro.MyByteSource;
import net.ysq.shiro.utils.SpringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.ObjectUtils;

/**
 *
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
     * 可以往Shiro中注册多种Realm。某种Token对象需要对应的Realm来处理。
     * 复写该方法表示该方法支持处理哪一种Token
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 从Token中获取身份信息。这里实际上是username，这里从UsernamePasswordToken的源码可以看出
        String principal = (String) token.getPrincipal();
        // 从IOC容器中获取UserService组件
        UserService userService = (UserService) SpringUtils.getBean("userService");

        User user = userService.findByUsername(principal);

        if (!ObjectUtils.isEmpty(user)) {
            // 返回正确的信息（数据库存储的），作为比较的基准
            return new SimpleAuthenticationInfo(
                    user, user.getPassword(),
                    new MyByteSource(user.getSalt()), this.getName()
            );
        }

        return null;
    }
}
