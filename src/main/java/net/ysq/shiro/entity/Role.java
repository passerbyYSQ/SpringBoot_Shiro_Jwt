package net.ysq.shiro.entity;


import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {

  private Integer id;
  private String roleName;

  private List<Permission> perms;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public List<Permission> getPerms() {
    return perms;
  }

  public void setPerms(List<Permission> perms) {
    this.perms = perms;
  }
}
