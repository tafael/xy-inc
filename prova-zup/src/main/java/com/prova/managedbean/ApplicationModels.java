package com.prova.managedbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.prova.model.Model;
import com.prova.service.FieldService;
import com.prova.service.ModelService;

@Named("ApplicationModels")
@ApplicationScoped
public class ApplicationModels implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ModelService modelService;

	@Inject
	private FieldService fieldService;

	private HashMap<String, Model> modelsMap;

	@PostConstruct
	private void init() {
		updateModels();
	}

	public void updateModels() {
		modelsMap = new HashMap<String, Model>();
		List<Model> models = modelService.findAll();
		for (Model model : models) {
			model.setFields(fieldService.findByModel(model));
			modelsMap.put(model.getName(), model);
		}
	}

	public HashMap<String, Model> getModelsMap() {
		return modelsMap;
	}

}
