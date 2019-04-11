package org.hippo.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
public class Clients implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private String id;

  private String clientId;

  private String clientSecret;

  private String resourceIds;

  private String scope;

  private String authorizedGrantTypes;

  private String webServerRedirectUri;

  private String authorities;

  private String accessTokenValidity;

  private String refreshTokenValidity;

  private String additionalInformation;

  private String autoapprove;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getResourceIds() {
    return resourceIds;
  }

  public void setResourceIds(String resourceIds) {
    this.resourceIds = resourceIds;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getAuthorizedGrantTypes() {
    return authorizedGrantTypes;
  }

  public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
    this.authorizedGrantTypes = authorizedGrantTypes;
  }

  public String getWebServerRedirectUri() {
    return webServerRedirectUri;
  }

  public void setWebServerRedirectUri(String webServerRedirectUri) {
    this.webServerRedirectUri = webServerRedirectUri;
  }

  public String getAuthorities() {
    return authorities;
  }

  public void setAuthorities(String authorities) {
    this.authorities = authorities;
  }

  public String getAccessTokenValidity() {
    return accessTokenValidity;
  }

  public void setAccessTokenValidity(String accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
  }

  public String getRefreshTokenValidity() {
    return refreshTokenValidity;
  }

  public void setRefreshTokenValidity(String refreshTokenValidity) {
    this.refreshTokenValidity = refreshTokenValidity;
  }

  public String getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public String getAutoapprove() {
    return autoapprove;
  }

  public void setAutoapprove(String autoapprove) {
    this.autoapprove = autoapprove;
  }

  @Override
  public String toString() {
    return "Clients{" +
        "id=" + id +
        ", clientSecret=" + clientSecret +
        ", resourceIds=" + resourceIds +
        ", scope=" + scope +
        ", authorizedGrantTypes=" + authorizedGrantTypes +
        ", webServerRedirectUri=" + webServerRedirectUri +
        ", authorities=" + authorities +
        ", accessTokenValidity=" + accessTokenValidity +
        ", refreshTokenValidity=" + refreshTokenValidity +
        ", additionalInformation=" + additionalInformation +
        ", autoapprove=" + autoapprove +
        "}";
  }
}
