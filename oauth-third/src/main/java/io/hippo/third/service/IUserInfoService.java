package io.hippo.third.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.hippo.third.exception.ElementExistException;
import io.hippo.third.params.UserParams;
import org.hippo.common.po.UserInfo;

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
