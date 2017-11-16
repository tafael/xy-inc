package com.prova.managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("MainMB")
@ViewScoped
public class MainMB implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@PostConstruct
	private void init() {
	}

}
