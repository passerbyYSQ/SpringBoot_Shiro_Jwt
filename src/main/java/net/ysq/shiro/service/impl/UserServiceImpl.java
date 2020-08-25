package net.ysq.shiro.service.impl;

import net.ysq.shiro.dao.RoleDAO;
import net.ysq.shiro.dao.UserDAO;
import net.ysq.shiro.entity.Permission;
import net.ysq.shiro.entity.Role;
import net.ysq.shiro.entity.User;
import net.ysq.shiro.service.UserService;
import net.ysq.shiro.utils.JwtUtil;
import net.ysq.shiro.utils.RandomUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2020-08-21 11:02
 */
@Service("userService") // 不要忘了
@Transactional // 开启事务。有需要再开启
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public void register(User user) {
        // 8个字符的随机字符串，作为加密的随机盐
        String salt = RandomUtil.generateStr(8);
        // 需要保存到数据库，第一次登录（认证）比较时需要使用
        user.setSalt(salt);

        // Md5Hash默认将随机盐拼接到源字符串的前面，然后使用md5加密，再经过x次的哈希散列
        // 第三个参数（hashIterations）：哈希散列的次数
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), user.getSalt(), 1024);
        user.setPassword(md5Hash.toHex());

        // 保存
        userDAO.save(user);
    }

    @Override
    public String generateJwt(String username) {
        // 8个字符的随机字符串，作为生成jwt的随机盐
        // 保证每次登录成功返回的Token都不一样
        String jwtSecret = RandomUtil.generateStr(8);
        // 将此次登录成功的jwt secret存到数据库，下次携带jwt时解密需要使用
        userDAO.updateJwtSecretByUsername(username, jwtSecret);
        return JwtUtil.generateJwt(username, jwtSecret);
    }

    @Override
    public void logout(String username) {
        userDAO.updateJwtSecretByUsername(username, "");
    }

    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    /**
     * 根据username查询用户的所有角色
     * @param username
     * @return
     */
    @Override
    public List<Role> getRolesByUsername(String username) {
        return userDAO.getRolesByUsername(username);
    }

    /**
     * 根据角色id查询权限集合
     * 调用RoleDao层，但业务层面，它属于User，可以写在UserService中
     * @param roleId
     * @return
     */
    @Override
    public List<Permission> getPermsByRoleId(Integer roleId) {
        return roleDAO.getPermsByRoleId(roleId);
    }


}
