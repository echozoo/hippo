package io.hippo.third.service;

import com.baomidou.mybatisplus.core.conditions.query.EmptyWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.hippo.third.exception.ElementExistException;
import io.hippo.third.params.UserParams;
import org.hippo.common.mapper.UserInfoMapper;
import org.hippo.common.po.UserInfo;
import org.hippo.common.util.EncryptionHelper;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

  @Autowired
  private UserInfoMapper userInfoMapper;

  @Override public int updateUser(Long id, UserParams userParams) throws ElementExistException {
    UserInfo dbUser = userInfoMapper.selectById(id);
    if (null == dbUser) {
      throw new ElementExistException(new StringBuffer(userParams.getAccount()).append(" not found").toString());
    }
    return userInfoMapper.updateById(paramToPo(userParams, dbUser));
  }

  private UserInfo paramToPo(UserParams userParams, UserInfo userInfo) {
    if (!userInfo.getAccount().equals(userParams.getAccount())) {
      userInfo.setAccount(userParams.getAccount());
    }
    if (!userInfo.getPassword().equals(userParams.getPassword())) {
      userInfo.setPassword(EncryptionHelper.encryptPasswordByBCrypt(userParams.getPassword()));
    }
    userInfo.setStatus(1);
    return userInfo;
  }

  public void updateUsers() {
    userInfoMapper.selectList(new EmptyWrapper<>()).forEach(it -> {
      UserParams userParams = new UserParams();
      userParams.setAccount(it.getAccount());
      userInfoMapper.updateById(paramToPo(userParams, it));
    });
  }
}
