package org.hippo.jwts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.hippo.common.po.SysPermission;
import org.hippo.common.po.SysRole;
import org.hippo.common.po.SysUserRole;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
public interface ISysUserRoleService extends IService<SysUserRole> {


  List<SysRole> userRoles(String username);

  List<SysPermission> userPermissions(String username);
}
