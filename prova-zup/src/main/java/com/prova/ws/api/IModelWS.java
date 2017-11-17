package com.prova.ws.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.prova.ws.client.GenericWSClient;
import com.prova.ws.client.annotation.WebTarget;

@WebTarget("/")
public interface IModelWS extends GenericWSClient {

	@GET
	@Path("/{model}")
	public List<Map<String, Object>> findAll(@PathParam("model") String modelName);

	@GET
	@Path("/{model}/{id}")
	public Map<String, Object> findById(@PathParam("model") String modelName, @PathParam("id") Long id);

	@POST
	@Path("/{model}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, Object> insert(@PathParam("model") String modelName, Map<String, Object> data);

	@PUT
	@Path("/{model}/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public String update(@PathParam("model") String modelName, @PathParam("id") Long id, Map<String, Object> data);

	@DELETE
	@Path("/{model}/{id}")
	public String delete(@PathParam("model") String modelName, @PathParam("id") Long id);

}
