package com.empmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.empmgmt.vo.Jobs;

public class SelectJobs {
	private static SelectJobs instance = null;

	// 싱글톤 패턴에서는 외부에서 기본생성자에 접근하지 못하도록 만들어야 한다.
	// 기본생성자를 안만들면 기본으로 만들어지는데 public하게 만들어지므로, private한 생성자가 필요하다면 이렇게 직접 만들어주어야한다.
	private SelectJobs() {
	}

	public static SelectJobs getInstance() {
		if (instance == null) {
			instance = new SelectJobs();
		}
		return instance;
	}
	
	public List<Jobs> selectJobsInfo() throws NamingException, SQLException{
		List<Jobs> jobList = new ArrayList<Jobs>();
		
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); // -스트림객체
		
		String query = "select * from jobs";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			jobList.add(new Jobs(
					rs.getString("JOB_ID"),
					rs.getString("JOB_TITLE"),
					rs.getInt("MIN_SALARY"),
					rs.getInt("MAX_SALARY")));
		}
		
		db.dbClose(rs, pstmt, con);
		
		return jobList;
	}
}
