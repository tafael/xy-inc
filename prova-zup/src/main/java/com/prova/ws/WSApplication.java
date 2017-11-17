package com.prova.ws;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.prova.util.Config;

/** Configurações dos webservices do sistema. */
@ApplicationPath(Config.WS_APPLICATION_PATH)
public class WSApplication extends Application {
}
