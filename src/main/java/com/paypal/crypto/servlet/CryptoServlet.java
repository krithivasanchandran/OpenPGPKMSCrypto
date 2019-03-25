package com.paypal.crypto.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import CryptoService.ServiceMain;


/**
 * Servlet implementation class CryptoServlet
 */
@WebServlet("/CryptoServlet")
public class CryptoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CryptoServlet() {
       System.out.println("Initializing Crypto Servlet !! ");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
		
		System.out.println("Servlet entry for POST Request here");
		
		String appname = request.getParameter("appname");
		String keyname = request.getParameter("keyname");
		String operationtype = request.getParameter("operationtype");
		String encryptiondocs = request.getParameter("encdocs");
		
		System.out.println(" appname  : " + appname + " --  "
				+ " keyname : " + keyname + " -- " + " Operation type " + operationtype + " -- " + " filepath " + encryptiondocs);
	
		
		try {	
			ServiceMain sc = new ServiceMain();
			String[] ops = new String[4];
			ops[0] = appname;
			ops[1] = keyname;
			ops[2] = operationtype;
			ops[3] = encryptiondocs;
			Map<String,String> keymakerResp = sc.serviceValidator(ops);
			
			if(keymakerResp != null){
				
				request.setAttribute("encodeformat", keymakerResp.get("encodeformat"));
				request.setAttribute("exportable", keymakerResp.get("exportable"));
				request.setAttribute("encryptable", keymakerResp.get("encryptable"));
				request.setAttribute("keyvalidity", keymakerResp.get("keyvalidity"));
				request.setAttribute("appname", keymakerResp.get("appname"));
				request.setAttribute("attributes",keymakerResp.get("attributes"));
				request.setAttribute("state", keymakerResp.get("state"));
				request.setAttribute("version", keymakerResp.get("version"));
				request.setAttribute("encodedKeyData", keymakerResp.get("encodedKeyData"));
				
				
				request.getRequestDispatcher("keymakerResponse.jsp").forward(request,response);
				
			}else{
				throw new Exception("KeyMaker API Response is Empty !!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
