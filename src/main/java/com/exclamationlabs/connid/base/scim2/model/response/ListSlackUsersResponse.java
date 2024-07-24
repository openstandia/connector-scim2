package com.exclamationlabs.connid.base.scim2.model.response;

import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.exclamationlabs.connid.base.scim2.model.slack.Scim2SlackUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class ListSlackUsersResponse extends ListUsersResponse<Scim2SlackUser> {
    @Override
    public Set<Scim2SlackUser> getResources() {
        return super.getResources();
    }

    @Override
    public void setResources(Set<Scim2SlackUser> resources) {
        super.setResources(resources);
    }
}
