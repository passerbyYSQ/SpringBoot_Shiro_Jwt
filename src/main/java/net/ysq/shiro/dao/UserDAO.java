package net.ysq.shiro.dao;

import net.ysq.shiro.po.Role;
import net.ysq.shiro.po.User;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2020-08-21 10:50
 */
public interface UserDAO {

    /**
     * 保存（插入、新增）User
     * @param user
     */
    void save(User user);

    /**
     * 根据username查询User信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据username更新jwt密钥
     * @param username
     */
    void updateJwtSecretByUsername(String username, String newJwtSecret);

    /**
     * 根据username查询用户的所有角色
     * @param username
     * @return
     */
    List<Role> getRolesByUsername(String username);



}
