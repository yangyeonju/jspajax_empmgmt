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

import com.empmgmt.dao.SelectDepartments;
import com.empmgmt.etc.ResponseJson;
import com.empmgmt.vo.Departments;


@WebServlet("/getDeptInfo.do")
public class GetDepartmentsInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public GetDepartmentsInfo() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 try {
			 List<Departments> lst = SelectDepartments.getInstance().selectDeptInfo();
			 String json = new ResponseJson().makeJsonStringDeptData(lst);
			 
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
