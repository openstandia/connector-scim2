package com.exclamationlabs.connid.base.scim2.model.response;

import com.exclamationlabs.connid.base.scim2.model.Scim2User;
import com.google.gson.annotations.SerializedName;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ListUsersResponse {
  @SerializedName("next_page_token")
  private String nextPageToken;

  @SerializedName("page_count")
  private Integer pageCount;

  @SerializedName("page_number")
  private Integer pageNumber;

  @SerializedName("page_size")
  private Integer pageSize;

  @SerializedName("total_records")
  private Integer totalRecords;

  private Set<Scim2User> users;
}
