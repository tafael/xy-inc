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

	public Model saveModel(Model model) {
		if (model.getId() != null) {
			Model oldModel = findById(model.getId());
			modelDAO.detach(oldModel);
			oldModel.setFields(fieldService.findByModel(oldModel));
			
			fieldService.saveUpdateDelete(oldModel.getFields(), model.getFields());
			
			Model newModel = saveOrUpdate(model);
			newModel.setFields(fieldService.findByModel(newModel));
			
			// atualiza a tabela do modelo.
			QueryGenerator generator = QueryGeneratorFactory.getQueryGenerator();
			List<String> statements = generator.generateAlterTable(oldModel, newModel);
			for (String statement : statements) {
				modelDAO.executeSQL(statement);
			}
			
			return newModel;
		} else {
			Model newModel = saveOrUpdate(model);
			
			Field id = new Field();
			id.setName("id");
			id.setType(TypeEnum.INTEGER);
			id.setNotNull(Boolean.TRUE);
			id.setModel(newModel);
			id = fieldService.saveOrUpdate(id);;
			
			newModel.setTableId(id);
			newModel = saveOrUpdate(newModel);
			
			for (Field field : model.getFields()) {
				field.setModel(newModel);
				fieldService.saveOrUpdate(field);
			}
			
			// cria a tabela para o novo modelo.
			QueryGenerator generator = QueryGeneratorFactory.getQueryGenerator();
			List<String> statements = generator.generateCreateTable(model);
			for (String statement : statements) {
				modelDAO.executeSQL(statement);
			}

			return newModel;
		}
	}
	
	public Map<String, Object> findDataById(Model model, Long id) {
		return modelDAO.findDataById(model, id);
	}

	public List<Map<String, Object>> findAllData(Model model) {
		return modelDAO.findAllData(model);
	}

	@Override
	protected GenericDAO<Model, Long> getGenericDAO() {
		return modelDAO;
	}

}
