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

@WebServlet("/remEmp.do")
public class RemoveEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public RemoveEmployee() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println(request.getParameter("remEmpNo") + "번 사원을 삭제하자" );
		//얘도 유저에게 입력받은거라 DTO
		int remEmpNo = Integer.parseInt(request.getParameter("remEmpNo"));
		
		 try {
			boolean result = EmployeesCRUD.getInstance().deleteEmployee(remEmpNo);
			
			String json = new ResponseJson().makeJsonStringIsSuccess(result);
			
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
