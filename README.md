# Connector Scim2

ConnID Connector to support [System for Cross-domain Identity Management (SCIM2)](https://www.rfc-editor.org/rfc/rfc7642.html) 
API for Identify and Access Management (IAM) of Remote Services

This open source connector for the SCIM2 API uses the [ConnId Framework](https://connid.tirasa.net/) 
for integration with Identity and Access Management (IAM) systems such as [Evolveum Midpoint](https://evolveum.com/midpoint/).

The software leverages the Connector Base Framework located at https://github.com/ExclamationLabs/connector-base

This software is Copyright 2024 Exclamation Graphics. Licensed under the Apache License, Version 2.0.



# Connector Configuration

The actual method of configuring a connector is largely dependent on the interface(s) provided by your Identity and
Access management system. Midpoint provides a convenient user interface method to enter these values. If configuration
properties are being read in from a property file you may also need to know the name of the property.

## SCIM2 Configuration Options

<table>
  <tr>
   <td>Service URL
   </td>
   <td>Yes
   </td>
   <td>The base URL of the SCIM2 endpoint. 
   </td>
  </tr>
  <tr>
   <td>IO Error Retries
   </td>
   <td>No
   </td>
   <td>Number of retries that will be attempted when an IO error occurs. Default is 5.
   </td>
  </tr>
  <tr>
   <td>Deep Get Enabled
   </td>
   <td>No
   </td>
   <td>When a search operation is executed and this value is <strong>true,</strong> the connector will make an individual call to download each User or Group returned. The value should be set to false since a standard SCIM2 service should return all attributes of each record.
   </td>
  </tr>
  <tr>
   <td>Deep Import Enabled
   </td>
   <td>No
   </td>
   <td>When an import operation is executed and this value is <strong>true</strong> the connector will attempt to download all attributes for each individual record returned. The option should be set to <strong>false</strong> since the SCIM2 connector should return all attributes on the search. 
   </td>
  </tr>
  <tr>
   <td>Import Batch Size
   </td>
   <td>No
   </td>
   <td>The default number of records to retrieve per page. Import operations will be invoked using the given batch size when it is supplied. Since the SCIM2 API supports paging you can import records one batch/page at a time instead of all at once.
   </td>
  </tr>
  <tr>
   <td>Pagination Enabled
   </td>
   <td>No
   </td>
   <td>The SCIM2 Connector supports pagination on User and Group objects. This option should be set to true.
   </td>
  </tr>
  <tr>
   <td>Duplicate Record Returns Id
   </td>
   <td>No
   </td>
   <td>When a create is attempted and an AlreadyExistsException is generated, the adapter shall attempt to return the id of the existing record matching the specified userName. 
   </td>
  </tr>
  <tr>
   <td>Resource Type URL
   </td>
   <td>string
   </td>
   <td>URL to Discover resource type for a particular service provider. These included Users and Groups URL endpoints. 
   </td>
  </tr>
  <tr>
   <td>Resource Type JSON
   </td>
   <td>string
   </td>
   <td>The actual resource types for a particular service provider
   </td>
  </tr>
  <tr>
   <td>Use Resource Type URL
   </td>
   <td>boolean
   </td>
   <td>Whether to use the URL or the JSON. Especially when the URL is not supported by the service provider. 
   </td>
  </tr>
  <tr>
   <td>Schema URL
   </td>
   <td>string
   </td>
   <td>URL to discover schema for a particular service provider
   </td>
  </tr>
  <tr>
   <td>Schema JSON
   </td>
   <td>string
   </td>
   <td>The actual Schema return for a particular service provider. <strong>This can be populated from the URL at discovery time.</strong>
   </td>
  </tr>
  <tr>
   <td>Use Schema URL
   </td>
   <td>boolean
   </td>
   <td>Where to use URL or JSON especially when URL is not available
   </td>
  </tr>
  <tr>
   <td>Enable Standard Schema
   </td>
   <td>boolean
   </td>
   <td>Uses prebuilt java objects based on the stand schema. 
   </td>
  </tr>
  <tr>
   <td>Enable Enterprise User
   </td>
   <td>boolean
   </td>
   <td>Extend the user schema with enterprise attributes 
   </td>
  </tr>
  <tr>
   <td>Enable AWS Schema
   </td>
   <td>boolean
   </td>
   <td>Use a pre-built java classes as defined for AWS \
As specified here https://docs.aws.amazon.com/singlesignon/latest/developerguide/what-is-scim.html 
   </td>
  </tr>
  <tr>
   <td>Enable Slack Schema
   </td>
   <td>boolean
   </td>
   <td>Use prebuilt java classes as define for Slack as specified here: \
https://api.slack.com/admins/scim2
   </td>
  </tr>
  <tr>
   <td>Enable Dynamic Schema
   </td>
   <td>boolean
   </td>
   <td>Use the Resource Type and/or the Schema defined above for dynamic operations. 
   </td>
  </tr>
  <tr>
   <td>Users Endpoint URL
   </td>
   <td>string
   </td>
   <td>Discovered from the resource type or entered manually. Usually “/Users
   </td>
  </tr>
  <tr>
   <td>Group Endpoint URL
   </td>
   <td>string
   </td>
   <td>Discovered from the resource type or entered manually. Usually “/Groups”
   </td>
  </tr>
  <tr>
   <td>UserSchemaIdList
   </td>
   <td>[string]
   </td>
   <td>
A list of user schemas that define a user.<br/>
This is discoverable from the Resource Type URL, JSON, or by one of the prebuilt java classes<br/>
Default:<br/>
urn:ietf:params:scim:schemas:core:2.0:User<br/>
urn:ietf:params:scim:schemas:extension:enterprise:2.0:User
   </td>
  </tr>
  <tr>
   <td>GroupSchemaIDList
   </td>
   <td>[string]
   </td>
   <td>A list of Group schemas that define a Group.<br/>
This is discoverable from the Resource Type URL or JSON. or by one of the prebuilt java classes
default:<br/>
urn:ietf:params:scim:schemas:core:2.0:Group+etc
   </td>
  </tr>
</table>

# References

1. [RFC 7642 SCIM2 Definitions, Overview, Concepts, and Requirements](https://datatracker.ietf.org/doc/html/rfc7642)
2. [RFC 7643 SCIM2 Core Schema](https://datatracker.ietf.org/doc/html/rfc7643)
3. [RFC 7644 SCIM2 Protocol](https://datatracker.ietf.org/doc/html/rfc7644)
4. [Amazon Web Services SCIM Implementation Guide](https://docs.aws.amazon.com/singlesignon/latest/developerguide/what-is-scim.html)
5. [Slack API Provisioning with SCIM 2.0](https://api.slack.com/admins/scim2)
6. [Zoom SCIM2 API](https://developers.zoom.us/docs/api/rest/reference/scim-api/methods/#overview)
7. [SalesForce SCIM2 Implementation](https://help.salesforce.com/s/articleView?id=sf.identity_scim_overview.htm&type=5)
8. [Fast Fed SCIM2 Interoperability Requirements](https://openid.net/specs/fastfed-scim-1_0-02.html#rfc.section.4)

