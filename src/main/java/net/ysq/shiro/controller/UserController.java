package net.ysq.shiro.controller;

import net.ysq.shiro.entity.User;
import net.ysq.shiro.service.UserService;
import net.ysq.shiro.utils.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通过注解实现权限控制
 * 在方法前添加注解 @RequiredRoles 或 @RequiredPermissions
 *
 *
 * @author passerbyYSQ
 * @create 2020-08-20 23:54
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("用户信息！这个接口需要携带有效的token才能访问");
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(User user) {
        // 参数判断省略
        // ...

        try {
            userService.register(user);
            // 注册成功，跳转到登录页面去认证
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 错误提示信息省略...
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("客户端传参错误");
    }

    /**
     * 用户登录（身份认证）
     * Shiro会缓存认证信息
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(String username, String password) {
        // 前期的注入工作已经由SpringBoot完成了
        // 获取主体对象
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(new UsernamePasswordToken(username, password));
            // 实际上就是User。为什么？看LoginRealm的SimpleAuthenticationInfo的传参
            User user = (User) subject.getPrincipal();

            // 签发jwt token
            String jwt = userService.generateJwt(user.getUsername());

            return ResponseEntity.ok().header("token", jwt).build();

        } catch (UnknownAccountException e) {
            // username 错误
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username不存在");
        } catch (IncorrectCredentialsException e) {
            // password 错误
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("password错误");
        }

        // 认证失败回到login.jsp
        // 可能会携带错误信息
//        return "redirect:/login.jsp";
    }

    /**
     * 退出登录
     * 销毁主体的认证记录（信息），下次访问需要重新认证
     *
     * @return
     */
    @RequestMapping("/logout")
    public ResponseEntity<String> logout() {
        Subject subject = SecurityUtils.getSubject();

        User user = (User) subject.getPrincipal();
        userService.logout(user.getUsername());
        subject.logout();

        return ResponseEntity.ok().build();
    }

}
