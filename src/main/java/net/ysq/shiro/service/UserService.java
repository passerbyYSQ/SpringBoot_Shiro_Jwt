package net.ysq.shiro.service;

import net.ysq.shiro.entity.Permission;
import net.ysq.shiro.entity.Role;
import net.ysq.shiro.entity.User;

import java.util.List;

/**
 * tip:
 *  idea转换大小写快捷键：ctrl + shift + u
 *
 * @author passerbyYSQ
 * @create 2020-08-21 11:01
 */
public interface UserService {

    /**
     * 注册的业务代码
     * @param user
     */
    void register(User user);

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

    /**
     * 根据username更新jwt密钥
     * @param username
     */
    String generateJwt(String username);

    void logout(String username);
}
