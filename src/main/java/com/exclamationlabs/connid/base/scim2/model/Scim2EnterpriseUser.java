package com.exclamationlabs.connid.base.scim2.model;

import java.util.Set;

public class Scim2EnterpriseUser
{
    private String employeeNumber;
    private String costCenter;
    private String division;
    private String department;
    private String organization;
    private  Manager manager;

    public static class Manager {
        String managerId;

    }

}
