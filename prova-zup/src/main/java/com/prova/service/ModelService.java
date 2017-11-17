package com.prova.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.prova.dao.GenericDAO;
import com.prova.dao.ModelDAO;
import com.prova.model.Field;
import com.prova.model.Model;
import com.prova.model.TypeEnum;
import com.prova.query.api.QueryGenerator;
import com.prova.query.api.QueryGeneratorFactory;

@Transactional
@Dependent
public class ModelService extends GenericService<Model, Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ModelDAO modelDAO;

	@Inject
	private FieldService fieldService;

	private QueryGenerator generator = QueryGeneratorFactory.getQueryGenerator();

	public Model saveModel(Model model) {
		if (model.getId() != null) {
			Model oldModel = findById(model.getId());
			modelDAO.detach(oldModel);
			oldModel.setFields(fieldService.findByModel(oldModel));
			
			fieldService.saveUpdateDelete(oldModel.getFields(), model.getFields());
			
			Model newModel = saveOrUpdate(model);
			newModel.setFields(fieldService.findByModel(newModel));
			
			// atualiza a tabela do modelo.
			modelDAO.executeSQL(generator.generateAlterTable(oldModel, newModel));
			
			return newModel;
		} else {
			Model newModel = saveOrUpdate(model);
			
			Field id = new Field();
			id.setName("id");
			id.setType(TypeEnum.INTEGER);
			id.setNotNull(Boolean.TRUE);
			id.setModel(newModel);
			id = fieldService.saveOrUpdate(id);
			
			newModel.setTableId(id);
			newModel = saveOrUpdate(newModel);
			
			for (Field field : model.getFields()) {
				field.setModel(newModel);
				fieldService.saveOrUpdate(field);
			}
			
			// cria a tabela para o novo modelo.
			modelDAO.executeSQL(generator.generateCreateTable(model));

			return newModel;
		}
	}

	public void deleteModel(Model model) {
		modelDAO.executeSQL(generator.generateDropTable(model));
		delete(model);
	}
	
	public Map<String, Object> findDataById(Model model, Long id) {
		return modelDAO.findDataById(model, id);
	}

	public List<Map<String, Object>> findAllData(Model model) {
		return modelDAO.findAllData(model);
	}
	

	public Map<String, Object> insertData(Model model, Map<String, Object> data) {
		return modelDAO.insertData(model, data);
	}
	
	public void updateData(Model model, Map<String, Object> data) {
		modelDAO.updateData(model, data);
	}
	
	public void deleteData(Model model, Object id) {
		modelDAO.deleteData(model, id);
		
	}

	@Override
	protected GenericDAO<Model, Long> getGenericDAO() {
		return modelDAO;
	}

}
