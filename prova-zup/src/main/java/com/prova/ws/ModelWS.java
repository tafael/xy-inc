package com.prova.ws;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Path;

import com.prova.managedbean.ApplicationModels;
import com.prova.model.Model;
import com.prova.service.ModelService;
import com.prova.util.Utils;
import com.prova.ws.api.IModelWS;

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
public class ModelWS extends GenericWS implements IModelWS {

	@Inject
	private ApplicationModels applicationModels;
	
	@Inject
	private ModelService modelService;

	private Model getModel(String model) {
		return applicationModels.getModelsMap().get(model);
	}
	
	public List<Map<String, Object>> findAll(String modelName) {
		return modelService.findAllData(getModel(modelName));
	}
	
	public Map<String, Object> findById(String modelName, Long id) {
		return modelService.findDataById(getModel(modelName), id);
	}

	public Map<String, Object> insert(String modelName, Map<String, Object> data) {
		Model model = getModel(modelName);
		Utils.convertDateTypes(model, data);
		return modelService.insertData(model, data);
	}
	
	public String update(String modelName, Long id, Map<String, Object> data) {
		data.put("id", id);
		Model model = getModel(modelName);
		Utils.convertDateTypes(model, data);
		modelService.updateData(model, data);
		return "success";
	}
	
	public String delete(String modelName, Long id) {
		modelService.deleteData(getModel(modelName), id);
		return "success";
	}

}

