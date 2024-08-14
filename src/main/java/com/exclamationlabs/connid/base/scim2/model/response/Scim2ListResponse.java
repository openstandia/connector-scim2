package com.exclamationlabs.connid.base.scim2.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;

/**
 * Typical "urn:ietf:params:scim:api:messages:2.0:ListResponse"
 */
public class Scim2ListResponse
{
    private Integer itemsPerPage;
    @SerializedName("Resources")
    private List<?> resources;
    private Set<String> schemas;
    private Integer startIndex;
    private Integer totalResults;

    public Integer getItemsPerPage()
    {
        return itemsPerPage;
    }

    public List<?> getResources()
    {
        return resources;
    }

    public Set<String> getSchemas()
    {
        return schemas;
    }

    public Integer getStartIndex()
    {
        return startIndex;
    }

    public Integer getTotalResults()
    {
        return totalResults;
    }

    public void setItemsPerPage(Integer itemsPerPage)
    {
        this.itemsPerPage = itemsPerPage;
    }

    public void setResources(List<?> resources)
    {
        this.resources = resources;
    }

    public void setSchemas(Set<String> schemas)
    {
        this.schemas = schemas;
    }

    public void setStartIndex(Integer startIndex)
    {
        this.startIndex = startIndex;
    }

    public void setTotalResults(Integer totalResults)
    {
        this.totalResults = totalResults;
    }
}
