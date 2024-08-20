package com.exclamationlabs.connid.base.scim2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Scim2Operation
{
    @SerializedName("op")
    private String operation;

    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public List<Map<String, String>> getValue()
    {
        return value;
    }

    public void setValue(List<Map<String, String>> value)
    {
        this.value = value;
    }

    private String path;
    private List<Map<String, String>> value;
}
