package org.hippo.jwts.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.List;
import org.hippo.jwts.po.SysRole;
import org.hippo.jwts.po.SysUserRole;
import org.hippo.jwts.po.UserInfo;
import org.hippo.jwts.service.SysRoleServiceImpl;
import org.hippo.jwts.service.SysUserRoleServiceImpl;
import org.hippo.jwts.service.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2018/9/19
 * @since JDK1.8
 * UserDetailsServiceImpl 作用只是从数据库获取用户信息
 */
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserInfoServiceImpl userInfoService;

  @Autowired
  private SysRoleServiceImpl sysRoleService;

  @Autowired
  private SysUserRoleServiceImpl sysUserRoleService;

  //
  @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //  验证用户名
    UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getAccount, username));
    if (null == userInfo) {
      throw new UsernameNotFoundException(new StringBuffer(username).append(": not found").toString());
    }
    Boolean unDeleted = true;
    if (null == userInfo.getStatus() || userInfo.getStatus() == 0) {
      unDeleted = false;
    }
    Boolean unLocked = true;
    if (null == userInfo.getStatus() || userInfo.getStatus() == 2) {
      unLocked = false;
    }
    Boolean enabled = true;
    if (null == userInfo.getStatus() || userInfo.getStatus() == 3) {
      enabled = false;
    }
    //    角色
    List<GrantedAuthority> roles = new ArrayList<>();
    List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userInfo.getId()));
    getRoles(sysUserRoles, roles);
    return new User(userInfo.getAccount(), userInfo.getPassword(), enabled, unDeleted, false, unLocked, roles);
  }

  /**
   * 获取所属角色
   *
   * @param sysUserRoles
   * @param roles
   */
  private void getRoles(List<SysUserRole> sysUserRoles, List<GrantedAuthority> roles) {
    sysUserRoles.forEach(it -> {
      SysRole role = sysRoleService.getById(it.getRoleId());
      roles.add(new SimpleGrantedAuthority(role.getName()));
    });
  }
}
