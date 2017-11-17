package com.prova.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.omnifaces.exceptionhandler.FullAjaxExceptionHandler;

public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;

	public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler result = new FullAjaxExceptionHandler(parent.getExceptionHandler());
		return result;
	}

}