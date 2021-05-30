package net.ysq.shiro.po;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Permission implements Serializable {

  private Integer id;
  private String permissionName;
  private String resourceUrl;




}
