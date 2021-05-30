package net.ysq.shiro.shiro;

import lombok.extern.slf4j.Slf4j;
import net.ysq.shiro.dto.ResultModel;
import net.ysq.shiro.dto.StatusCode;
import net.ysq.shiro.utils.JsonUtils;
import net.ysq.shiro.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author passerbyYSQ
 * @create 2020-08-22 12:06
 */
@Slf4j
public class JwtAuthenticatingFilter extends BasicHttpAuthenticationFilter {

    // 是否刷新token
    private boolean shouldRefreshToken;

    public JwtAuthenticatingFilter() {
        this.shouldRefreshToken = false;
    }

    /**
     * AccessControlFilter的onPreHandle：isAccessAllowed || onAccessDenied
     * 请求是否允许放行
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) { //not found any token
            log.info("Not found any token");
        } catch (Exception e) {
            log.info("Error occurs when login");
        }
        return allowed;
        //return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        Subject subject = SecurityUtils.getSubject();
//        subject.logout(); // 清除缓存

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
        PrintWriter writer = response.getWriter();
        String json = JsonUtils.objectToJson(ResultModel.failed(StatusCode.AUTHENTICATION_FAILED));
        writer.print(json);
        return false;
    }

    /**
     * 父类executeLogin()首先会createToken()，然后调用shiro的Subject.login()方法。
     * executeLogin()的逻辑是不是跟UserController里面的密码登录逻辑很像？
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
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) {

        String oldToken = (String) token.getPrincipal();
        Date expireAt = JwtUtils.getExpireAt(oldToken);

        if (shouldRefreshToken) {
            // 刷新token...
        }

        return true;
    }

    public boolean isShouldRefreshToken() {
        return shouldRefreshToken;
    }

    public void setShouldRefreshToken(boolean shouldRefreshToken) {
        this.shouldRefreshToken = shouldRefreshToken;
    }
}
