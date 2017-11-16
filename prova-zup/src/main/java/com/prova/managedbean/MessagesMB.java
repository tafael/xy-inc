package com.prova.managedbean;

import java.io.Serializable;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

@Named("MessagesMB")
@ViewScoped
public class MessagesMB implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String title;
	
	private String message;
	
	private String cancelAction;
	
	private String confirmAction;
	
	public void showMessage(String title, String message) {
		this.title = title;
		this.message = message;
		RequestContext.getCurrentInstance().update("dialogMessages");
		RequestContext.getCurrentInstance().execute("PF('dlgMessages').show();");
	}
	
	public void showConfirmation(String title, String message, String cancelAction, String confirmAction) {
		this.title = title;
		this.message = message;
		this.cancelAction = cancelAction;
		this.confirmAction = confirmAction;
		RequestContext.getCurrentInstance().update("dialogConfirmation");
		RequestContext.getCurrentInstance().execute("PF('dlgConfirmation').show();");
	}
	
	public void executeCancel() {
		if (cancelAction != null) {
			invoke(cancelAction);
		}
		RequestContext.getCurrentInstance().execute("PF('dlgConfirmation').hide();");
	}
	
	public void executeConfirm() {
		if (confirmAction != null) {
			invoke(confirmAction);
		}
		RequestContext.getCurrentInstance().execute("PF('dlgConfirmation').hide();");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static void invoke(String expression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		MethodExpression methodExpression = facesContext.getApplication().getExpressionFactory().createMethodExpression(facesContext.getELContext(), expression, null, new Class<?>[] {}); 
		methodExpression.invoke(facesContext.getELContext(), null);
	}

}
