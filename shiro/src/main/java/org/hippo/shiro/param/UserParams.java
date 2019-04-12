package org.hippo.shiro.param;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author <a href="http://github.com/athc">dujf</a>
 * @date 2019-04-10
 * @since JDK1.8
 */
public class UserParams {

  @ApiModelProperty(required = true, name = "账号")
  private String account;

  @ApiModelProperty(required = true, name = "密码")
  private String password;

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
