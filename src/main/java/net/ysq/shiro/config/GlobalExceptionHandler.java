package net.ysq.shiro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ysq.shiro.dto.ResultModel;
import net.ysq.shiro.dto.StatusCode;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获
 *
 * @author passerbyYSQ
 * @create 2020-11-02 23:27
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 注入容器中的ObjectMapper（被我们定制过的）
    @Resource
    private ObjectMapper objectMapper;

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ParameterErrorException.class})
    public ModelAndView handleMyrException(Exception e, HttpServletRequest request) { // 注意写基类！！！
        ResultModel res = ResultModel.failed(StatusCode.PARAMETER_INVALID.getCode(), e.getMessage());
        return wrapModelAndView(res, request);
    }

    /*
    // jwt异常
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(JWTVerificationException.class)
    public ModelAndView handlerJwtVerificationException(JWTVerificationException e, HttpServletRequest request) {
        // 主要分为两类错误：token过期；无效token
        ResultModel res = ResultModel.failed(e instanceof TokenExpiredException ?
                        StatusCode.TOKEN_EXPIRED : StatusCode.TOKEN_INVALID); // token过期，登录状态过期
        return wrapModelAndView(res, request);
    }
    */

    /**
     * 被shiro内部所捕获了，SpringMVC捕获不到。
     * 只能在JwtAuthenticatingFilter的onAccessDenied()方法处理认证失败的异常
     */
    /*
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class) // 注意是shiro包下
    public ModelAndView handlerAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        StatusCode code;
        if (e instanceof AccountException) {
            code = StatusCode.ACCOUNT_INCORRECT; // 账户错误
        } else if (e instanceof CredentialsException) {
            code = StatusCode.CREDENTIAL_INCORRECT; // 凭证错误（密码错误）
        } else {
            code = StatusCode.AUTHENTICATION_FAILED; // 其它认证失败
        }
        return wrapModelAndView(ResultModel.failed(code), request);
    }
    */

    // 授权异常
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView handlerAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        return wrapModelAndView(ResultModel.failed(StatusCode.AUTHORIZATION_FAILED), request);
    }

    // 前后端联调时和正式上线后开启
    // 后端编码时，为了方便测试，先注释掉
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 非业务层面的异常，表示出现了服务端错误。
    @ExceptionHandler({Exception.class})
    public ModelAndView handleOtherException(Exception e, HttpServletRequest request) {
        e.printStackTrace(); // 打印到控制台，方便调试
        ResultModel<Object> res = ResultModel.failed(
                StatusCode.UNKNOWN_ERROR.getCode(), e.toString());
        return wrapModelAndView(res, request);
    }

    private ModelAndView wrapModelAndView(ResultModel resultModel, HttpServletRequest request) {
        ModelAndView modelAndView = isApiRequest(request) ?
                new ModelAndView(new MappingJackson2JsonView(objectMapper)) :
                new ModelAndView("error/500"); // 自己定制500页面。略...
        modelAndView.addObject("code", resultModel.getCode());
        modelAndView.addObject("msg", resultModel.getMsg());
        modelAndView.addObject("data", resultModel.getData());
        modelAndView.addObject("time", resultModel.getTime());
        return modelAndView;
    }

    /**
     * 判断出错的API方法是返回json数据还是页面
     * 出错的API方法有ResponseBody注解，表示接口返回json数组。否则表示接口渲染页面
     */
    private boolean isApiRequest(HttpServletRequest request) {
        return request.getHeader("Accept") == null ||
                !request.getHeader("Accept").contains("text/html");
    }
}
