package com.prova.ws;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public abstract class GenericWS {

	@Context
	protected HttpServletRequest request;

	@Context
	protected HttpServletResponse response;

	@PostConstruct
	private void init() {
	}

}