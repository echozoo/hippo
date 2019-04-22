package org.hippo.jwts.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.hippo.common.mapper.SysPermissionMapper;
import org.hippo.common.mapper.SysRoleMapper;
import org.hippo.common.mapper.SysRolePermissionMapper;
import org.hippo.common.mapper.SysUserRoleMapper;
import org.hippo.common.mapper.UserInfoMapper;
import org.hippo.common.po.SysPermission;
import org.hippo.common.po.SysRole;
import org.hippo.common.po.SysRolePermission;
import org.hippo.common.po.SysUserRole;
import org.hippo.common.po.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

  private final SysUserRoleMapper sysUserRoleMapper;

  private final SysRoleMapper sysRoleMapper;

  private final SysPermissionMapper sysPermissionMapper;

  private final SysRolePermissionMapper sysRolePermissionMapper;

  private final UserInfoMapper userInfoMapper;

  @Autowired public SysUserRoleServiceImpl(SysUserRoleMapper sysUserRoleMapper, SysRoleMapper sysRoleMapper, SysPermissionMapper sysPermissionMapper, SysRolePermissionMapper sysRolePermissionMapper, UserInfoMapper userInfoMapper) {
    this.sysUserRoleMapper = sysUserRoleMapper;
    this.sysRoleMapper = sysRoleMapper;
    this.sysPermissionMapper = sysPermissionMapper;
    this.sysRolePermissionMapper = sysRolePermissionMapper;
    this.userInfoMapper = userInfoMapper;
  }

  @Override public List<SysRole> userRoles(String username) {
    UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getAccount, username));
    //查角色
    List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userInfo.getId()));
    List<SysRole> roles = sysRoleMapper.selectList(
        new LambdaQueryWrapper<SysRole>()
            .in(SysRole::getId, sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList())
            )
    );
    return roles;
  }

  @Override public List<SysPermission> userPermissions(String username) {
    UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getAccount, username));

    List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, userInfo.getId()));

    //查角色权限
    List<SysRolePermission> rolePermissionList = sysRolePermissionMapper.selectList(
        new LambdaQueryWrapper<SysRolePermission>()
            .in(SysRolePermission::getRoleId, sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList())
            )
    );
    //查权限
    return sysPermissionMapper.selectList(
        new LambdaQueryWrapper<SysPermission>()
            .in(SysPermission::getId, rolePermissionList.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList())
            )
    );
  }

}
