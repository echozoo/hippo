package org.hippo.jwts.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hippo.common.mapper.UserInfoMapper;
import org.hippo.common.po.UserInfo;
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


}
