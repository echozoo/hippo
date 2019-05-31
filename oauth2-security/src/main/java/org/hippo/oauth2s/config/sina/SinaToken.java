package org.hippo.oauth2s.config.sina;

import lombok.Data;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-05-30
 * @since JDK1.8
 * {access_token=2.00S6ZjAGqbplQCad812f037dJVpPdC, remind_in=157679999, expires_in=157679999, uid=5507657860, isRealName=true}
 */
@Data
public class SinaToken {
  private String accessToken;
  private String remindIn;
  private int expiresIn;
  private String uid;
  private String isRealName;

}
