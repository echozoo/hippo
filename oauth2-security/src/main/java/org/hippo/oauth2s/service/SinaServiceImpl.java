package org.hippo.oauth2s.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hippo.common.po.Clients;
import org.hippo.oauth2s.config.oauth2.SignInHelper;
import org.hippo.oauth2s.config.sina.SinaConfig;
import org.hippo.oauth2s.config.sina.SinaToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-05-30
 * @since JDK1.8
 */
@Service
public class SinaServiceImpl implements SinaService {

  private final RestTemplate restTemplate;

  private final ClientsServiceImpl clientsService;

  private final SignInHelper signInHelper;

  @Autowired public SinaServiceImpl(RestTemplate restTemplate, SignInHelper signInHelper, ClientsServiceImpl clientsService) {
    this.restTemplate = restTemplate;
    this.signInHelper = signInHelper;
    this.clientsService = clientsService;
  }

  /**
   * https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI
   *
   * @return
   */
  @Override public String getSinaCodeUrl() {
    String symbol = "&";
    String question = "?";
    String sinaCodeUrl = new StringBuffer(SinaConfig.authorizeUrl)
        .append(question)
        .append("client_id=")
        .append(SinaConfig.appKey)
        .append(symbol)
        .append("response_type=code")
        .append(symbol)
        .append("redirect_uri=")
        .append(SinaConfig.redirectUrl)
        .toString();
    logger.info(sinaCodeUrl);
    return sinaCodeUrl;
  }

  /**
   * https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE
   *
   * @param code
   */
  @Override public SinaToken getSinaToken(String code) {
    String symbol = "&";
    String question = "?";
    logger.info(code);

    String tokenUrl = new StringBuffer(SinaConfig.accessTokenUrl)
        .append(question)
        .append("client_id=")
        .append(SinaConfig.appKey)
        .append(symbol)
        .append("client_secret=")
        .append(SinaConfig.appSecret)
        .append(symbol)
        .append("grant_type=authorization_code")
        .append(symbol)
        .append("code=")
        .append(code)
        .append(symbol)
        .append("redirect_uri=")
        .append(SinaConfig.redirectUrl)
        .toString();
    logger.info(tokenUrl);
    Map result = restTemplate.postForEntity(tokenUrl, new HashMap<String, String>(16), Map.class).getBody();
    String token = result.get("access_token").toString();
    int expiresIn = (Integer) result.get("expires_in");
    String isRealName = result.get("isRealName").toString();
    String uid = result.get("uid").toString();
    String remindIn = result.get("remind_in").toString();
    SinaToken sinaToken = new SinaToken();
    sinaToken.setAccessToken(token);
    sinaToken.setExpiresIn(expiresIn);
    sinaToken.setIsRealName(isRealName);
    sinaToken.setUid(uid);
    sinaToken.setRemindIn(remindIn);
    return sinaToken;
  }

  @Override public OAuth2AccessToken getSinaUserInfo(SinaToken sinaToken) {
    String symbol = "&";
    String question = "?";
    logger.info(sinaToken.getAccessToken());
    String tokenUrl = new StringBuffer(SinaConfig.userInfoUrl)
        .append(question)
        .append("access_token=")
        .append(sinaToken.getAccessToken())
        .append(symbol)
        .append("uid=")
        .append(sinaToken.getUid())
        .toString();
    Map result = restTemplate.getForEntity(tokenUrl, Map.class).getBody();
    logger.info(result.toString());
    //todo:存用户信息
    Clients clients = clientsService.getOne(new QueryWrapper<Clients>().lambda().eq(Clients::getClientId, "user"));
    Set scope = new HashSet<String>();
    scope.add(clients.getScope());
    List authorities = new ArrayList<SimpleGrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority(clients.getAuthorities()));
    return signInHelper.signIn(clients.getClientId(), result, authorities, scope);
  }


  private Logger logger = LoggerFactory.getLogger(SinaServiceImpl.class);
}
