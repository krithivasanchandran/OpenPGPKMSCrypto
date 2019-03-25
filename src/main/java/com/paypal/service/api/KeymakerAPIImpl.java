package com.paypal.service.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class KeymakerAPIImpl implements KeymakerAPI{

	@GET
	@Path("/world")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayhello(){
		return "hello welcome Jersey";
	}
	
	@Override
	public String encrypt() {
		return null;
	}

	@Override
	public String decrypt() {
		// TODO Auto-generated method stub
		return null;
	}

}
