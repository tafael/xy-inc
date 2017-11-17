package com.prova.ws.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.prova.util.Utils;
import com.prova.ws.model.ErrorMessage;

@Provider
public class WSExceptionHandler implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable ex) {
		Response response;
		if (ex instanceof WebApplicationException) {
			WebApplicationException webEx = (WebApplicationException) ex;
			response = webEx.getResponse();
		} else {
			String message = "";
			if (!Utils.isEmpty(ex.getMessage())) {
				message = " - " + ex.getMessage();
			}
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro Interno" + message)).build();
		}
		return response;
	}

}
