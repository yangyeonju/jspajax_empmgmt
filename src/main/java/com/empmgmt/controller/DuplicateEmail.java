package com.empmgmt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.empmgmt.dao.EmployeesCRUD;
import com.empmgmt.etc.ResponseJson;


@WebServlet("/duplicateEmail.do")
public class DuplicateEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DuplicateEmail() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("userInputEmail");
		//System.out.println(userInputEmail);
		
		try {
			
			boolean result = EmployeesCRUD.getInstance().isDuplicateEmail(email);
			String json = new ResponseJson().makeJsonStringDuplicateEmail(result);
			
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(json);
			out.flush(); 
			out.close();
			
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
