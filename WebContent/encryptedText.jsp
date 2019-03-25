<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Encrypted Text</title>
</head>
<body>
<%
   String enc = (String)request.getAttribute("encrypted"); 
%>

<table style="margin:0 auto; padding-top:120px;">
<h1 style="text-align: center; padding-top:80px;"> Encrypted Text:</h1>

<tr><td><strong> Encrypted Text :</strong></td>
<td><textarea rows = "10" cols = "38" name = "encrypted"><%= enc%></textarea></td>
</tr>
<tr></tr><tr></tr>
<% 
if(request.getAttribute("decrypted") != null){ 
   String dec = (String) request.getAttribute("decrypted");
%>
<tr><td><strong> Decrypted Text :</strong></td>
<td><textarea rows = "10" cols = "38" name = "decrypted"><%= dec%></textarea></td></tr>
<%}else{ %>
<tr><td><h4> No!! Decrypted Text Found</h4></td></tr>
<%} %>
</table>
<br/>
<br/>

<%

 if(request.getAttribute("integrity") != null){
	 String integrityString = (String) request.getAttribute("integrity");

%>

<h3 style="color: red; text-align: center;">File Integrity Check :=><b><%= integrityString%></b></h3>

<% }else{ %>

<h3 style="text-align:center; color: red;"> File Integrity Check is not applicable here</h3>

<%} %>

</body>
</html>