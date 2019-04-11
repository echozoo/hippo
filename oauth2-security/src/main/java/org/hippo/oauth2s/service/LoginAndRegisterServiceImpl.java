package org.hippo.oauth2s.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.hippo.common.mapper.SysUserRoleMapper;
import org.hippo.common.mapper.UserInfoMapper;
import org.hippo.common.po.SysUserRole;
import org.hippo.common.po.UserInfo;
import org.hippo.common.util.EncryptionHelper;
import org.hippo.oauth2s.config.oauth2.ClientDetailsServiceImpl;
import org.hippo.oauth2s.exception.ElementExistException;
import org.hippo.oauth2s.params.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author dujf
 */
@Service
@Transactional
public class LoginAndRegisterServiceImpl implements ILoginAndRegisterService {

  @Autowired
  private UserInfoMapper userInfoMapper;

  @Autowired
  private ClientDetailsServiceImpl clientDetailsService;

  @Autowired
  private TokenEndpoint tokenEndpoint;

  @Autowired
  private SysUserRoleMapper sysUserRoleMapper;

  @Override public OAuth2AccessToken login(HttpServletRequest request, UserParams userParams) throws HttpRequestMethodNotSupportedException {
    String s = request.getHeader("Authorization");
    if (StringUtils.isBlank(s)) {
      throw new ClientRegistrationException(new StringBuffer("clientId").append(" not found").toString());
    }
    String[] ss = null;
    try {
      ss = new String(Base64.getDecoder().decode(s.replace("basic", "").trim()), "utf-8").split(":");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    if (ss.length < 2) {
      throw new ClientRegistrationException(new StringBuffer("clientId: ").append(ss[0]).append(" not found").toString());
    }
    ClientDetails clientDetails = clientDetailsService.loadClientByClientId(ss[0]);
    Authentication authentication = new UsernamePasswordAuthenticationToken(ss[0], ss[1], clientDetails.getAuthorities());
    Map params = new HashMap();
    params.put("username", userParams.getAccount());
    params.put("password", userParams.getPassword());
    params.put("grant_type", "password");
    params.put("scope", "read");
    return (OAuth2AccessToken) tokenEndpoint.postAccessToken(authentication, params).getBody();
  }

  @Override public int register(UserParams userParams) throws ElementExistException {
    //重复判断
    UserInfo dbUser = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getAccount, userParams.getAccount()));
    if (null != dbUser) {
      throw new ElementExistException(userParams.getAccount() + " already exist");
    }
    //添加用户
    UserInfo userInfo = paramToPo(userParams);
    userInfoMapper.insert(userInfo);
    //添加角色
    SysUserRole sysUserRole = new SysUserRole();
    sysUserRole.setUserId(userInfo.getId());
    sysUserRole.setRoleId(4);
    return sysUserRoleMapper.insert(sysUserRole);
  }

  private UserInfo paramToPo(UserParams userParams) {
    UserInfo userInfo = new UserInfo();
    userInfo.setAccount(userParams.getAccount());
    userInfo.setPassword(EncryptionHelper.encryptPasswordByBCrypt(userParams.getPassword()));
    userInfo.setStatus(1);
    return userInfo;
  }
}
