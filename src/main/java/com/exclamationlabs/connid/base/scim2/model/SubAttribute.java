package com.exclamationlabs.connid.base.scim2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import javax.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "name",
  "type",
  "multiValued",
  "description",
  "required",
  "caseExact",
  "mutability",
  "returned",
  "uniqueness",
  "canonicalValues",
  "referenceTypes"
})
@Generated("jsonschema2pojo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubAttribute {

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("multiValued")
  private Boolean multiValued;

  @JsonProperty("description")
  private String description;

  @JsonProperty("required")
  private Boolean required;

  @JsonProperty("caseExact")
  private Boolean caseExact;

  @JsonProperty("mutability")
  private String mutability;

  @JsonProperty("returned")
  private String returned;

  @JsonProperty("uniqueness")
  private String uniqueness;

  @JsonProperty("canonicalValues")
  private List<String> canonicalValues;

  @JsonProperty("referenceTypes")
  private List<String> referenceTypes;
}
