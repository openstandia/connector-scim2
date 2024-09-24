package com.exclamationlabs.connid.base.scim2.model.response.fault;

public interface ErrorResponseCode
{
    int HTTP_BAD_REQUEST = 400;
    int HTTP_CONFLICT = 409;
    int HTTP_NOT_FOUND = 404;
    int HTTP_UNAUTHORIZED = 401;
    int HTTP_FORBIDDEN = 403;
    int HTTP_INTERNAL_SERVER_ERROR = 500;
    int HTTP_NOT_IMPLEMENTED = 501;
    int HTTP_BAD_GATEWAY = 502;
    int HTTP_SERVICE_UNAVAILABLE = 503;
}
