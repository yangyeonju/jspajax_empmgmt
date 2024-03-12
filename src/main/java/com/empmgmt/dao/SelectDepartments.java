package com.empmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.jsp.jstl.sql.Result;

import com.empmgmt.vo.Departments;

public class SelectDepartments {
	private static SelectDepartments instance = null;
	
	private SelectDepartments() {
		
	}
	
	public static SelectDepartments getInstance() {
		if(instance == null) {
			instance = new SelectDepartments();
		}
		return instance;
	}
	
	public List<Departments> selectDeptInfo() throws NamingException, SQLException{
		
		List<Departments> lst = new ArrayList<Departments>();
		
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen();
		
		String q = "select * from departments";
		
		PreparedStatement pstmt = con.prepareStatement(q);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			lst.add(new Departments(
					rs.getInt("DEPARTMENT_ID"),
					rs.getString("DEPARTMENT_NAME"),
					rs.getInt("MANAGER_ID"),
					rs.getInt("LOCATION_ID")));
		}
		
//		for(Departments d : lst) {
//			System.out.println(d.toString());
//		}
		
		//닫았다는 얘기는 정상종료 되었다는 뜻 (commit)
		db.dbClose(rs, pstmt, con);
		
		return lst;
		
	}
}
