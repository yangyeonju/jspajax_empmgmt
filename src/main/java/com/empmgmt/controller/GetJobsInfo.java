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

import com.empmgmt.dao.SelectJobs;
import com.empmgmt.etc.ResponseJson;
import com.empmgmt.vo.Jobs;


@WebServlet("/getJobsInfo.do")
public class GetJobsInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public GetJobsInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Jobs> jobList = SelectJobs.getInstance().selectJobsInfo();
			String json = new ResponseJson().makeJsonStringJobsData(jobList);
			System.out.println(json);
			
			response.setContentType("application/json; charset=utf-8");;
			PrintWriter out = response.getWriter();
			out.print(json);
//			
			out.flush(); // 버퍼 비우기. 버퍼가 딱 맞아떨어지지 않는다. 큰컵에 담긴 물을 작은컵(버퍼)가 나르는데 그게 딱 맞아떨어지지 않음
			out.close();
			
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
