package net.ysq.shiro.service;

import net.ysq.shiro.po.Permission;
import net.ysq.shiro.po.Role;
import net.ysq.shiro.po.User;

import java.util.List;

/**
 * tip:
 *  idea转换大小写快捷键：ctrl + shift + u
 *
 * @author passerbyYSQ
 * @create 2020-08-21 11:01
 */
public interface UserService {


    void register(String username, String password);

    User login(String username, String password);

    String generateJwt(User user);

    void logout(String username);

    User findByUsername(String username);

    /**
     * 由于业务层没有什么其他操作，所以直接跟Dao层同名，直接调用即可
     * @param username
     * @return
     */
    List<Role> getRolesByUsername(String username);

    /**
     * 根据角色id查询权限集合
     * 调用RoleDao层，但业务层面，它属于User，可以写在UserService中
     * @param roleId
     * @return
     */
    List<Permission> getPermsByRoleId(Integer roleId);
}
