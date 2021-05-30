package net.ysq.shiro.po;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class User implements Serializable {

  private Integer id;
  private String username;
  private String password;
  // 加密登录密码的随机盐，注册时候为每一个用户生成随机串
  private String salt;
  // 生成jwt时的随机盐，刷新token的时候会更新
  private String jwtSecret;

  private List<Role> roles;

//  @Override
//  public String toString() {
//    return this.getClass().getName() + "{" +
//            "id=" + id +
//            ", username='" + username + '\'' +
//            ", password='" + password + '\'' +
//            ", salt='" + salt + '\'' +
//            ", jwtSecret='" + jwtSecret + '\'' +
//            ", roles=" + roles +
//            '}';
//  }
}
