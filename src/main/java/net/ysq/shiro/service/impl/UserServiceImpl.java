package net.ysq.shiro.service.impl;

import net.ysq.shiro.dao.RoleDAO;
import net.ysq.shiro.dao.UserDAO;
import net.ysq.shiro.po.Permission;
import net.ysq.shiro.po.Role;
import net.ysq.shiro.po.User;
import net.ysq.shiro.service.UserService;
import net.ysq.shiro.utils.JwtUtils;
import net.ysq.shiro.utils.RandomUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author passerbyYSQ
 * @create 2020-08-21 11:02
 */
@Service("userService") // 不要忘了
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    @Resource
    private RoleDAO roleDAO;

    @Override
    public void register(String username, String password) {
        User user = new User();
        // 8个字符的随机字符串，作为加密登录的随机盐。
        String salt = RandomUtil.generateStr(8);
        // 保存到user表，以后每次密码登录的时候，需要使用
        user.setSalt(salt);

        // Md5Hash默认将随机盐拼接到源字符串的前面，然后使用md5加密，再经过x次的哈希散列
        // 第三个参数（hashIterations）：哈希散列的次数
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), user.getSalt(), 1024);
        // 保存加密后的密码
        user.setPassword(md5Hash.toHex());

        // 保存
        userDAO.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null; // 用户名不存在
        }
        // 以注册时的相同规则，加密用户输入的密码
        Md5Hash md5Hash = new Md5Hash(password, user.getSalt(), 1024);
        // 比对密码
        return user.getPassword().equals(md5Hash.toHex()) ? user : null;
    }

    @Override
    public String generateJwt(User user) {
        // 8个字符的随机字符串，作为生成jwt的随机盐
        // 保证每次登录成功返回的Token都不一样
        String jwtSecret = RandomUtil.generateStr(8);
        // 将此次登录成功的jwt secret存到数据库，下次携带jwt时解密需要使用
        userDAO.updateJwtSecretByUsername(user.getUsername(), jwtSecret);
        return JwtUtils.generateJwt("username", user.getUsername(),
                jwtSecret, TimeUnit.SECONDS.toMillis(60 * 60)); // 有效期 60s
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
