package com.prova.ws;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.prova.managedbean.ApplicationModels;
import com.prova.model.Model;
import com.prova.service.ModelService;

/**
 A partir desse cadastro os seguintes recursos REST para gerenciamento desse modelo
deveriam estar disponíveis:

GET /xxx - Lista todos os elementos do Modelo XXX
GET /xxx/{id} - Busca um registro do modelo XXX por id

POST /xxx - Cria um novo registro do modelo XXX
PUT /xxx/{id} - Edita um registro do modelo XXX
DELETE /xxx/{id} - Deleta um registo do modelo XXX
 * */

@Path("/")
public class ModelWS extends GenericWS {

	@Inject
	private ApplicationModels applicationModels;
	
	@Inject
	private ModelService modelService;

	private Model getModel(String model) {
		return applicationModels.getModelsMap().get(model);
	}
	
	@GET
	@Path("/{model}")
	public List<Map<String, Object>> findAll(@PathParam("model") String modelName) {
		return modelService.findAllData(getModel(modelName));
	}
	
	@GET
	@Path("/{model}/{id}")
	public Map<String, Object> findById(@PathParam("model") String modelName, @PathParam("id") Long id) {
		return modelService.findDataById(getModel(modelName), id);
	}

	@POST
	@Path("/{model}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, String> insert(@PathParam("model") String modelName, Map<String, String> data) {
		return null;
	}
	
	@PUT
	@Path("/{model}/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(@PathParam("model") String modelName, @PathParam("id") Long id, Map<String, String> data) {
		data.put("id", id.toString());
	}
	
	@DELETE
	@Path("/{model}/{id}")
	public void delete(@PathParam("model") String modelName, @PathParam("id") Long id) {
		
	}

}

