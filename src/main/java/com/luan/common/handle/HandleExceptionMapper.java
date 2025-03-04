package com.luan.common.handle;

import com.luan.common.handle.rest.response.ProblemDetails;
import com.luan.common.mapper.ErrorResponseMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public abstract class HandleExceptionMapper<T extends Exception> {

    @Context
    UriInfo uriInfo;

    @Inject
    ErrorResponseMapper mapper;

    protected String getType() {
        return "about:blank";
    }

    abstract String getTitle();

    String getDetail() {
        return null;
    }

    abstract int getStatus();

    public Response toResponse(T exception) {
        ProblemDetails errorResponse = buildResponse(exception, uriInfo);
        return getResponse(errorResponse);
    }

    protected ProblemDetails buildResponse(T exception, UriInfo uriInfo) {
        String type = getType();
        String title = getTitle();
        int status = getStatus();
        String detail = getDetail() != null
                ? getDetail()
                : (exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage());
        String instance = uriInfo.getRequestUri().toString();
        return new ProblemDetails(type, title, status, detail, instance);
    }

    protected Response getResponse(ProblemDetails errorResponse) {
        int status = errorResponse.getStatus();
        String mediaType = MediaType.APPLICATION_JSON;
        return Response.status(status).entity(errorResponse).type(mediaType).build();
    }

}
