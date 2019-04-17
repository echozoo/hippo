package org.hippo.jwts.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hippo.common.po.SysPermission;
import org.hippo.common.po.SysRole;
import org.hippo.common.po.SysRolePermission;
import org.hippo.common.po.SysUserRole;
import org.hippo.common.po.UserInfo;
import org.hippo.jwts.service.SysPermissionServiceImpl;
import org.hippo.jwts.service.SysRolePermissionServiceImpl;
import org.hippo.jwts.service.SysRoleServiceImpl;
import org.hippo.jwts.service.SysUserRoleServiceImpl;
import org.hippo.jwts.service.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dellll
 */
@Component
public class MyShiroRealm extends AuthorizingRealm {

  @Autowired
  private UserInfoServiceImpl userInfoService;

  @Autowired
  private SysUserRoleServiceImpl sysUserRoleService;

  @Autowired
  private SysRolePermissionServiceImpl sysRolePermissionService;

  @Autowired
  private SysPermissionServiceImpl sysPermissionService;

  @Autowired
  private SysRoleServiceImpl sysRoleService;


  /**
   * 认证信息.(身份验证)
   * :
   * Authentication 是用来验证用户身份
   *
   * @param token
   * @return
   * @throws AuthenticationException
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    //获取用户的输入的账号.
    String username = (String) token.getPrincipal();
    String password = new String((char[]) token.getCredentials());
    UserInfo backUser = userInfoService.getOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getAccount, username));
    if (backUser == null) {
      throw new UnknownAccountException("用户名或密码不正确");
    }
    //账号禁用
    if (backUser.getStatus() == 2) {
      throw new LockedAccountException("账号已被禁用,请联系管理员");
    }
    return new SimpleAuthenticationInfo(backUser, password, getName());
  }


  /**
   * 此方法调用  hasRole,hasPermission的时候才会进行回调.
   * <p>
   * 权限信息.(授权):
   * 1、如果用户正常退出，缓存自动清空；
   * 2、如果用户非正常退出，缓存自动清空；
   * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
   * （需要手动编程进行实现；放在service进行调用）
   * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，
   * 调用clearCached方法；
   * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
   *
   * @param principals
   * @return
   */
  @Override public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    /*
     * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
     * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
     * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
     * 缓存过期之后会再次执行。
     */
    UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    //查角色
    List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userInfo.getId()));
    List<SysRole> roles = sysRoleService.list(
        new LambdaQueryWrapper<SysRole>()
            .in(SysRole::getId, sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList())
            )
    );
    //查角色权限
    List<SysRolePermission> rolePermissionList = sysRolePermissionService.list(
        new LambdaQueryWrapper<SysRolePermission>()
            .in(SysRolePermission::getRoleId, sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList())
            )
    );
    //查权限
    List<SysPermission> sysPermissions = sysPermissionService.list(
        new LambdaQueryWrapper<SysPermission>()
            .in(SysPermission::getId, rolePermissionList.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList())
            )
    );
    Set<String> permissions = new HashSet<>();
    for (SysPermission permission : sysPermissions) {
      permissions.add(permission.getPermission());
    }
    //设置角色信息
    authorizationInfo.setRoles(roles.stream().map(SysRole::getName).collect(Collectors.toSet()));
    //设置权限信息
    authorizationInfo.setStringPermissions(permissions);
    return authorizationInfo;
  }

}