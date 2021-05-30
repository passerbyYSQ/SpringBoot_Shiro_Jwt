package net.ysq.shiro.dto;

/**
 * 2000 - 成功处理请求
 * 3*** - 重定向，需要进一步的操作已完成请求
 * 4*** - 客户端错误，请求参数错误，语法错误等等
 * 5*** - 服务器内部错误
 * ...
 *
 * @author passerbyYSQ
 * @create 2020-11-02 16:26
 */
// 不加上此注解，Jackson将对象序列化为json时，直接将枚举类转成它的名字
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusCode {
    SUCCESS(2000, "成功"),

    // 服务器内部错误
    UNKNOWN_ERROR(5000, "未知错误"),

    // 认证相关
    TOKEN_INVALID(6000, "无效token"),
    TOKEN_EXPIRED(6001, "token已过期，请重新登录"),
    ACCOUNT_INCORRECT(6002, "账号错误"),
    CREDENTIAL_INCORRECT(6003, "密码错误"), // 凭证错误
    AUTHENTICATION_FAILED(6004, "认证失败，无效token或token已过期"),
    // 授权相关
    AUTHORIZATION_FAILED(6005, "无权限访问"),

    LOGIN_FAILED(6020, "登录失败，检查账号或密码是否错误"),
    PARAMETER_INVALID(6021, "参数错误")

    ;


    // 状态码数值
    private Integer code;
    // 状态码描述信息
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据业务状态码获取对应的描述信息
     * @param code      业务状态码
     * @return
     */
    public static String getMsgByCode(Integer code) {
        for (StatusCode status : StatusCode.values()) {
            if (status.code.equals(code)) {
                return status.msg;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
