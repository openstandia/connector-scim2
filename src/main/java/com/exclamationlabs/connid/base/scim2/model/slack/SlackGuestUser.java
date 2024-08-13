package com.exclamationlabs.connid.base.scim2.model.slack;

import java.util.Set;

public class SlackGuestUser {
  private Set<SlackChannel> channels;
  private String expiration;
  private String type;

  public Set<SlackChannel> getChannels()
  {
    return channels;
  }

  public String getExpiration()
  {
    return expiration;
  }

  public String getType()
  {
    return type;
  }

  public void setChannels(Set<SlackChannel> channels)
  {
    this.channels = channels;
  }

  public void setExpiration(String expiration)
  {
    this.expiration = expiration;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}
