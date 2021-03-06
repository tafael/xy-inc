package com.prova.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.prova.model.Field;
import com.prova.model.Model;
import com.prova.model.TypeEnum;
import com.prova.service.FieldService;
import com.prova.service.ModelService;
import com.prova.util.Utils;

@Named("ModelsMB")
@ViewScoped
public class ModelsMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ApplicationModels applicationModels;

	@Inject
	private ModelService modelService;

	@Inject
	private FieldService fieldService;

	@Inject
	private MessagesMB messagesMB;

	// Model
	private List<Model> models;
	private Model model;
	private boolean editing;

	// Field
	private Field field;
	private boolean editingField;

	@PostConstruct
	private void init() {
		models = modelService.findAll();
	}

	private void loadModels() {
		models = modelService.findAll();
		applicationModels.updateModels();
	}

	private boolean validate() {
		if (!Utils.isValidSQLName(model.getName())) {
			messagesMB.showMessage("Erro", "Nome inválido. O nome do modelo deve começar com uma letra. Não pode haver espaços nem caracteres especiais.");
			return false;
		}
		
		Model existingModel = applicationModels.getModelsMap().get(model.getName());
		if (existingModel != null && !existingModel.equals(model)) {
			messagesMB.showMessage("Erro", "Já existe um outro modelo com esse nome.");
			return false;
		}
		return true;
	}

	public void newModel() {
		this.editing = false;
		this.model = new Model();
		this.model.setFields(new ArrayList<Field>());
	}

	public void editModel(Model model) {
		this.editing = true;
		this.model = model;
		this.model.setFields(fieldService.findByModel(model));
	}

	public void newField() {
		this.editingField = false;
		this.field = new Field();
		this.field.setModel(this.model);
	}

	public void editField(Field field) {
		this.field = field;
		this.editingField = true;
	}

	public boolean validateField() {
		if (!Utils.isValidSQLName(field.getName())) {
			messagesMB.showMessage("Erro", "Nome inválido. O nome do campo deve começar com uma letra. Não pode haver espaços nem caracteres especiais.");
			return false;
		}
		if (!editingField && field.getName().equals("id")) {
			messagesMB.showMessage("Erro", "O nome 'id' está reservado para o identificador do Modelo. Utilize outro nome para o campo.");
			return false;
		}
		for (Field f : model.getFields()) {
			if (field != f && field.getName().equals(f.getName())) {
				messagesMB.showMessage("Erro", "Já existe um outro campo com esse nome.");
				return false;
			}
		}
		return true;
	}

	public void addField() {
		if (!validateField()) return;
		this.model.getFields().add(field);
		RequestContext.getCurrentInstance().update("formEdit");
		RequestContext.getCurrentInstance().execute("PF('dlgEditField').hide();");
	}

	public void confirmField() {
		if (!validateField()) return;
		RequestContext.getCurrentInstance().update("formEdit");
		RequestContext.getCurrentInstance().execute("PF('dlgEditField').hide();");
	}

	public void removeField() {
		this.model.getFields().remove(field);
		RequestContext.getCurrentInstance().update("formEdit");
		RequestContext.getCurrentInstance().execute("PF('dlgEditField').hide();");
	}

	public void confirmSave() {
		if (!validate()) return;
		if (editing) {
			messagesMB.showConfirmation("Salvar Model", "Deseja salvar as alterações feitas?", null, "#{ModelsMB.save()}");
		} else {
			save();
		}
	}

	public void save() {
		if (!validate()) return;
		model = modelService.saveModel(model);
		loadModels();
		RequestContext.getCurrentInstance().update("form");
		RequestContext.getCurrentInstance().execute("PF('dlgEdit').hide();");
	}

	public void confirmDelete() {
		messagesMB.showConfirmation("Excluir Modelo", "Deseja realmente excluir este modelo?", null, "#{ModelsMB.delete()}");
	}

	public void delete() {
		modelService.deleteModel(model);
		loadModels();
		RequestContext.getCurrentInstance().update("form");
		RequestContext.getCurrentInstance().execute("PF('dlgEdit').hide();");
	}

	public TypeEnum[] getTypes() {
		return TypeEnum.values();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public List<Model> getModels() {
		return models;
	}

	public boolean isEditing() {
		return editing;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public boolean isEditingField() {
		return editingField;
	}

	public void setEditingField(boolean editingField) {
		this.editingField = editingField;
	}

}
