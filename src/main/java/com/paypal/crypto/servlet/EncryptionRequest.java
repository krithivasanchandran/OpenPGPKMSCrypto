package com.paypal.crypto.servlet;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.openpgp.PGPException;

import KeyMakerService.PGPEncryptionPassword;
import KeyMakerService.PGPPrivateKeyDecryption;

/**
 * Servlet implementation class EncryptionRequest
 */
@WebServlet("/EncryptionRequest")
public class EncryptionRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EncryptionRequest() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Calling the " + this.getClass().getName());
		doGet(request, response);
		
		String appname = request.getParameter("appname");
		String appstate = request.getParameter("appstate");
		String encodedKey = request.getParameter("encodedkey");
		
		StringBuilder builder = new StringBuilder();
		builder.append("Recieved the POST request details from the servlet");
		builder.append("appname ---> ").append(":").append(appname).append("\n");
		builder.append("app state --> ").append(": ").append(appstate).append("\n");
		builder.append("encodedkey ---> ").append(" : ").append(encodedKey).append("\n");
		System.out.println(builder.toString());
		
		try {
			
			if(appname.equalsIgnoreCase("finapp_test_private")){
				
				Map<String,String> decrypted = PGPPrivateKeyDecryption.decryptPGP(encodedKey);
				String s =  decrypted.get("encrypted");
				String t =  decrypted.get("decrypted");
				String integrityStatus = decrypted.get("integrity");
				
				request.setAttribute("encrypted",s);
				request.setAttribute("decrypted",t);
				request.setAttribute("integrity", integrityStatus);
				
				request.getRequestDispatcher("encryptedText.jsp").forward(request,response);

			}else{
				
				Map<String,byte[]> result = PGPEncryptionPassword.pinto(encodedKey);
				String enc = new String(result.get("encrypted"));
				String dec = new String(result.get("decrypted"));
				
				request.setAttribute("encrypted", enc);
				request.setAttribute("decrypted", null);

				request.getRequestDispatcher("encryptedText.jsp").forward(request,response);
			}
			
			
			
		} catch (PGPException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			System.out.println(e.getMessage());
		} catch (Exception m) {
			System.out.println(m.getMessage());
		}
		
	}

}
