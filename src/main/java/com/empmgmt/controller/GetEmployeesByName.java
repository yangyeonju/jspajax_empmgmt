package com.empmgmt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.empmgmt.dao.SelectEmployees;
import com.empmgmt.etc.ResponseJson;
import com.empmgmt.vo.Employees;


@WebServlet("/getEmployeeByName.do")
public class GetEmployeesByName extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public GetEmployeesByName() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//매개변수 수집
		// System.out.println(request.getParameter("searchName"));
		String findName = request.getParameter("searchName");
		
		try {
			List<Employees> lst = SelectEmployees.getInstance().selectEmployeesBySearchName(findName);
			
			String json = new ResponseJson().makeJsonStringEntireEmployeesData(lst);
		
			System.out.println(json);
			
			//response 객체를 통해 json을 출력한다.
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
