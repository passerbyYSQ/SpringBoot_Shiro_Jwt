package net.ysq.shiro.po;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Role implements Serializable {

  private Integer id;
  private String roleName;

  private List<Permission> perms;

}
