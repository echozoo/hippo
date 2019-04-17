package org.hippo.jwts.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.hippo.common.mapper.UserInfoMapper;
import org.hippo.common.po.UserInfo;
import org.hippo.common.util.EncryptionHelper;
import org.hippo.jwts.config.jwt.JWtTokenAuthenticationService;
import org.hippo.jwts.param.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dujf
 */
@Service
public class LoginAndRegisterServiceImpl implements ILoginAndRegisterService {

  private final UserInfoMapper userInfoMapper;

  private final DefaultSecurityManager securityManager;

  private final JWtTokenAuthenticationService jWtTokenAuthenticationService;

  @Autowired public LoginAndRegisterServiceImpl(UserInfoMapper userInfoMapper, DefaultSecurityManager securityManager, JWtTokenAuthenticationService jWtTokenAuthenticationService) {
    this.userInfoMapper = userInfoMapper;
    this.securityManager = securityManager;
    this.jWtTokenAuthenticationService = jWtTokenAuthenticationService;
  }

  @Override public String login(UserParams params) {

    UserInfo backUser = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getAccount, params.getAccount()));
    if (backUser == null) {
      throw new UnknownAccountException("用户名或密码不正确");
    }
    if (!EncryptionHelper.encryptPasswordByBCrypt(params.getPassword()).equals(backUser.getPassword())) {
      throw new IncorrectCredentialsException("用户名或密码不正确");
    }
    //将SecurityManager创建到生成环境中
    SecurityUtils.setSecurityManager(securityManager);
    //认证提交认证token
    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(params.getAccount(), params.getPassword());
    Subject subject = SecurityUtils.getSubject();
    //执行认证提交认证
    try {
      subject.login(usernamePasswordToken);
      return jWtTokenAuthenticationService.addAuthentication(params.getAccount());
    } catch (AuthenticationException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
