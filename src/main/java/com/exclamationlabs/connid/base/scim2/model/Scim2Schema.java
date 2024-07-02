package com.exclamationlabs.connid.base.scim2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
// @JsonPropertyOrder({"id", "name", "description", "attributes", "meta"})
// @JsonPropertyOrder({"Resources"})
@Generated("jsonschema2pojo")
@Data
// @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scim2Schema {

  /*@JsonProperty("schemas")
  public List<String> schemas;
  @JsonProperty("Resources")
  public List<Resource> resources;
  public static class Resource{
    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("description")
    public String description;

    @JsonProperty("attributes")
    public List<Attribute> attributes;
    private Map<String, Object> extensions = new HashMap<>();

    @JsonProperty("meta")
    public Meta meta;
  }*/

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("attributes")
  public List<Attribute> attributes;

  @JsonProperty("meta")
  private Meta meta;

  public static class Attribute {
    @JsonProperty("name")
    public String name;

    @JsonProperty("type")
    public String type;

    @JsonProperty("multiValued")
    public Boolean multiValued;

    @JsonProperty("description")
    public String description;

    @JsonProperty("required")
    public Boolean required;

    @JsonProperty("caseExact")
    public Boolean caseExact;

    @JsonProperty("mutability")
    public String mutability;

    @JsonProperty("returned")
    public String returned;

    @JsonProperty("uniqueness")
    public String uniqueness;

    @JsonProperty("subAttributes")
    public List<Attribute> subAttributes;

    // @JsonProperty("referenceTypes")
    // public List<String> referenceTypes;
  }
}
