
package com.prova.managedbean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.prova.model.Field;
import com.prova.model.Model;
import com.prova.service.FieldService;
import com.prova.service.ModelService;
import com.prova.util.Utils;
import com.prova.ws.api.IModelWS;
import com.prova.ws.client.WSClientFactory;

@Named("TestRestMB")
@ViewScoped
public class TestRestMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ModelService modelService;

	@Inject
	private FieldService fieldService;

	@Inject
	private MessagesMB messagesMB;
	
	private IModelWS modelWSClient;
	
	private Model model;
	
	private List<Map<String, Object>> dataList;
	
	private Map<String, Object> data;
	
	private boolean editing;
	
	@PostConstruct
	private void init() {
		Long idModel = Long.parseLong(Utils.getRequestParameter("model"));
		model = modelService.findById(idModel);
		model.setFields(fieldService.findByModel(getModel()));
		modelWSClient = WSClientFactory.getModelWSClient();
		loadData();
	}
	
	private void loadData() {
		dataList = modelWSClient.findAll(model.getName());
		Utils.convertDateTypes(model, dataList);
	}

	public void newData() {
		this.data = new HashMap<String, Object>();
		this.editing = false;
	}

	public void edit(Map<String, Object> data) {
		this.data = data;
		this.editing = true;
	}

	public boolean validate() {
		for (Field field: model.getFields()) {
			if (!model.getTableId().equals(field) && field.getNotNull() && data.get(field.getName()) == null) {
				messagesMB.showMessage("Error", "O campo " + field.getName() + " é obrigatório.");
				return false;
			}
		}
		return true;
	}
	
	public void confirmSave() {
		if (!validate()) return;
		if (editing) {
			messagesMB.showConfirmation("Salvar Dados", "Deseja salvar as alterações feitas?", null, "#{TestRestMB.save()}");
		} else {
			save();
		}
	}

	public void save() {
		if (!validate()) return;
		if (editing) {
			modelWSClient.update(model.getName(), ((Number) data.get(model.getTableId().getName())).longValue(), data);
		} else {
			modelWSClient.insert(model.getName(), data);
		}
		loadData();
		RequestContext.getCurrentInstance().update("form");
		RequestContext.getCurrentInstance().execute("PF('dlgEdit').hide();");
	}

	public void confirmDelete() {
		messagesMB.showConfirmation("Excluir Dados", "Deseja realmente excluir esta linha?", null, "#{TestRestMB.delete()}");
	}

	public void delete() {
		modelWSClient.delete(model.getName(), ((Number) data.get(model.getTableId().getName())).longValue());
		loadData();
		RequestContext.getCurrentInstance().update("form");
		RequestContext.getCurrentInstance().execute("PF('dlgEdit').hide();");
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

}
