package fr.afcepf.atod.ws.currency.biz.rest;

import java.io.IOException;
import org.apache.log4j.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
 
@Provider
@PreMatching
public class RESTCorsDemoResponseFilter implements ContainerResponseFilter {
 
    private final static Logger log = Logger.getLogger( RESTCorsDemoResponseFilter.class.getName() );
 
    @Override
    public void filter( ContainerRequestContext requestCtx, ContainerResponseContext responseCtx ) throws IOException {
        log.info( "Executing REST response filter" );
 
        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With" );
    }
}
