package io.hippo.third.params;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author dujf
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
