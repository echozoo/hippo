package org.hippo.oauth2s.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hippo.common.po.UserInfo;
import org.hippo.oauth2s.exception.ElementExistException;
import org.hippo.oauth2s.params.UserParams;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
public interface IUserInfoService extends IService<UserInfo> {

  int updateUser(Long id, UserParams userParams) throws ElementExistException;

}
