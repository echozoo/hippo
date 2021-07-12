package io.hippo.third.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.hippo.third.params.RoleParams;
import java.util.List;
import org.hippo.common.mapper.SysRoleMapper;
import org.hippo.common.mapper.SysUserRoleMapper;
import org.hippo.common.po.SysRole;
import org.hippo.common.po.SysUserRole;
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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

  @Autowired
  private SysUserRoleMapper sysUserRoleMapper;

  @Override public void addRole(List<RoleParams> roleParams) {
    roleParams.forEach(it -> sysUserRoleMapper.insert(param2Po(it)));
  }

  private SysUserRole param2Po(RoleParams roleParams) {
    SysUserRole sysUserRole = new SysUserRole();
    sysUserRole.setRoleId(roleParams.getRoleId());
    sysUserRole.setUserId(roleParams.getUserId());
    return sysUserRole;
  }
}
