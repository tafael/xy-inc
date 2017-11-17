package com.prova.ws.client;

import org.jboss.resteasy.client.exception.ResteasyClientException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.prova.util.Config;
import com.prova.ws.api.IModelWS;
import com.prova.ws.client.annotation.WebTarget;

public final class WSClientFactory {

	private WSClientFactory() {
	}

	public static <T extends GenericWSClient> T getClient(Class<T> clazz, Class<?> ... componentsClasses) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		String webTargetPath = "";
		WebTarget webtargetAnnotation = clazz.getAnnotation(WebTarget.class);
		if (webtargetAnnotation == null) {
			throw new ResteasyClientException("'com.prova.ws.client.annotation.WebTarget' indefinido para o WebService: " + clazz.getName());
		} else {
			webTargetPath = Config.HOST_URL + Config.WS_APPLICATION_PATH + webtargetAnnotation.value();
		}
		ResteasyWebTarget target = client.target(webTargetPath);
		
		if (componentsClasses != null) {
			for (Class<?> componentClass : componentsClasses) {
				target.register(componentClass);
			}
		}
		
        T proxy = target.proxy(clazz);
        return proxy;
	}

	public static IModelWS getModelWSClient() {
		return getClient(IModelWS.class);
	}

}
