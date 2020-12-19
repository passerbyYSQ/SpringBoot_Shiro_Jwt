package net.ysq.shiro.shiro;

import net.ysq.shiro.entity.User;
import net.ysq.shiro.service.UserService;
import net.ysq.shiro.utils.ApplicationContextUtil;
import net.ysq.shiro.utils.DateTimeUtil;
import net.ysq.shiro.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author passerbyYSQ
 * @create 2020-08-22 12:06
 */
public class JwtAuthenticatingFilter extends BasicHttpAuthenticationFilter {

    // 是否刷新token
    private boolean shouldRefreshToken;

    public JwtAuthenticatingFilter() {
        this.shouldRefreshToken = false;
    }

    /**
     * AccessControlFilter的onPreHandle：isAccessAllowed || onAccessDenied
     *
     * 请求是否允许放行
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch(IllegalStateException e){ //not found any token
            System.out.println("Not found any token");
        }catch (Exception e) {
            System.out.println("Error occurs when login");
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 如果这个Filter在之前isAccessAllowed（）方法中返回false,则会进入这个方法。我们这里直接返回错误的response
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
        PrintWriter writer = response.getWriter();
        writer.print("无效token");
        //fillCorsHeader(request, httpResponse);
        return false;
    }

    /**
     * 父类executeLogin()首先会createToken()，然后调用shiro的Subject.login()方法。
     *
     * executeLogin()的逻辑是不是跟UserController里面的密码登录逻辑很像？
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 从请求头中的Authorization字段尝试获取jwt token
        String token = httpRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            // 从请求头中的token字段（自定义字段）尝试获取jwt token
            token = httpRequest.getHeader("token");
        }
        if (StringUtils.isEmpty(token)) {
            // 从url参数中尝试获取jwt token
            token = httpRequest.getParameter("token");
        }

        if (!StringUtils.isEmpty(token)) {
            return new JwtToken(token);
        }

        return null;
    }

    /**
     * 登录成功后判断是否需要刷新token
     * 登录成功说明：jwt有效，尚未过期。当离过期时间不足一天时，往响应头中放入新的token返回给前端
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) {

        String oldToken = (String) token.getPrincipal();

        Date expireAt = JwtUtil.getExpireAt(oldToken);
        int countDownDays = (int) DateTimeUtil.differDaysBetween(
                LocalDateTime.now(), DateTimeUtil.toLocalDateTime(expireAt));

        if (shouldRefreshToken && !ObjectUtils.isEmpty(expireAt)
             && countDownDays < 1) {  // 如果离过期时间不足一天

            UserService userService = (UserService) ApplicationContextUtil.getBean("userService");
            User user = (User) subject.getPrincipal();
            String newToken = userService.generateJwt(user.getUsername());
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("token", newToken);
        }

        return true;
    }

    /**
     * 添加跨域支持。在这里设置跨域不包括登录和注册路径
     * 所以，我们需要通过SpringBoot来设置全局的跨域！！！
     *
     * @param request
     * @param response
     * @throws Exception
     * @return
     */
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        return fillCorsHeader(request, response);
//    }

    /**
     * 设置跨域
     */
//    public boolean fillCorsHeader(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        httpResponse.setHeader("Access-control-Allow-Origin", httpRequest.getHeader("Origin"));
//        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
//        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpResponse.setStatus(HttpStatus.OK.value());
//            return false;
//        }
//        return super.preHandle(request, response);
//    }

    public boolean isShouldRefreshToken() {
        return shouldRefreshToken;
    }

    public void setShouldRefreshToken(boolean shouldRefreshToken) {
        this.shouldRefreshToken = shouldRefreshToken;
    }
}
