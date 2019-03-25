package com.paypal.service.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*
 * Generic Restful Service Template - Bouncy Castle KMS.
 * Please extend or edit it to your convenience.
 */

@Path("/googlekms/v1")
public class KeymakerAPIImpl implements KeymakerAPI{

	@GET
	@Path("/testbed")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayhello(){
		return "welcome to google KMS";
	}
	
	@Override
	public <T> encrypt<T> {
	   return method(<T>);
	}

	@Override
	public String decrypt() {
	   return method(<T>);
	}

}
