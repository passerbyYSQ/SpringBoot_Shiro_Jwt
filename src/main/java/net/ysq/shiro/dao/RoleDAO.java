package net.ysq.shiro.dao;

import net.ysq.shiro.po.Permission;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2020-08-21 23:31
 */
public interface RoleDAO {
    /**
     * 根据roleId查询角色的所有权限
     * @param roleId
     * @return
     */
    List<Permission> getPermsByRoleId(Integer roleId);
}
