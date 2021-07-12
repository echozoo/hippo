package io.hippo.third.config.oauth2;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.hippo.third.service.SysRoleServiceImpl;
import io.hippo.third.service.SysUserRoleServiceImpl;
import io.hippo.third.service.UserInfoServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.hippo.common.po.SysRole;
import org.hippo.common.po.SysUserRole;
import org.hippo.common.po.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final UserInfoServiceImpl userInfoService;

  private final SysRoleServiceImpl sysRoleService;

  private final SysUserRoleServiceImpl sysUserRoleService;

  @Autowired public UserDetailsServiceImpl(SysRoleServiceImpl sysRoleService, UserInfoServiceImpl userInfoService, SysUserRoleServiceImpl sysUserRoleService) {this.sysRoleService = sysRoleService;
    this.userInfoService = userInfoService;
    this.sysUserRoleService = sysUserRoleService;
  }

  //
  @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("---------------UserDetailsServiceImpl-----------------");
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
    return new User(userInfo.getAccount(), userInfo.getPassword(), enabled, unDeleted, true, unLocked, roles);
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

  private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
}
