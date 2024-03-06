<%@page import="com.empmgmt.dao.DBConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>test.jsp</h1>
	
	<%
		DBConnection con = new DBConnection();
		out.print(con.dbOpen().toString());
	%>
</body>
</html>