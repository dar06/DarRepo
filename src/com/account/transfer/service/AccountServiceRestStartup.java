package com.account.transfer.service;

import com.account.transfer.service.impl.RevolutService;
import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;


public class AccountServiceRestStartup {

    private static final int PORT = 8080;
    private static final String HOST = "http://localhost/";

    public static void main(String[] args) {
        final URI uri = UriBuilder.fromUri(HOST).port(PORT).build();
        final ResourceConfig config = new ResourceConfig(RevolutService.class);
        final HttpServer httpServer = JdkHttpServerFactory.createHttpServer(uri, config);
    }
}
