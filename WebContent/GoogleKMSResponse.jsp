<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GOOGLE KMS Response</title>
</head>

<body style="background-color: aqua;">
<%
   String encodeformat = (String)request.getAttribute("encodeformat"); 
   String exportable = (String) request.getAttribute("exportable");
   String encryptable = (String) request.getAttribute("encryptable");
   String keyvalidity = (String)request.getAttribute("keyvalidity");
   String appname = (String)request.getAttribute("appname");
   String attributes = (String)request.getAttribute("attributes");
   String state = (String)request.getAttribute("state");
   String version = (String)request.getAttribute("version");
   String encodedKeyData = (String)request.getAttribute("encodedKeyData");
   
%>
<h1 style="text-align: center; padding-top:80px;"> KeyMaker Response for APP NAME => <strong><%= appname %></strong></h1>
<form method="post" action=EncryptionRequest>
<table style="margin:0 auto; padding-top:90px;">
<tr>
 <td><strong> Encode Format : </strong></td>
 <td> <input type="text" value="<%= encodeformat%>" size="40"> </td>
</tr>
<tr>
<td><strong> Is App Exportable : </strong></td>
<td> <input type="text" value="<%= exportable %>" size="40"></td>
</tr>
<tr>
<td><strong> Is Encryptable : </strong></td>
<td> <input type="text" value="<%= encryptable %>" size="40"></td>
</tr>
<tr>
<td><strong> KeyValidity : </strong></td>
<td> <input type="text" value="<%= keyvalidity %>" size="40"></td>
</tr>
<tr>
<td><strong>AppName : </strong></td>
<td> <input type="text" name="appname" value="<%= appname %>" size="40"></td>
</tr>
<tr>
<td><strong>Attributes :</strong></td>
<td> <input type="text" value="<%= attributes %>" size="40"></td>
</tr>
<tr>
<td><strong> State : </strong></td>
<td> <input type="text" name="appstate"  value="<%= state %>" size="40" ></td>
</tr>
<tr>
<td><strong> Version : </strong></td>
<td> <input type="text" value="<%= version %>" size="40"></td>
</tr>
<tr>
<td><strong> EncodedKeyData : </strong></td>
<td><textarea rows = "10" cols = "38" name = "encodedkey"><%= encodedKeyData%></textarea></td>
</tr>

</table>
<br/><br/>
  <input style="margin-left:640px;" type="submit" value="Submit">
</form>
</body>
</html>
