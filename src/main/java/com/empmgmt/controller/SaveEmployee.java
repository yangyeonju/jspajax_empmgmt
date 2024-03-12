package com.empmgmt.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.tribes.group.Response;

import com.empmgmt.dao.EmployeesCRUD;
import com.empmgmt.etc.ResponseJson;
import com.empmgmt.vo.Employees;

@WebServlet("/saveEmployee.do")
public class SaveEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SaveEmployee() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//post방식이기 때문에 
		request.setCharacterEncoding("utf-8");
		//filter라는 걸 만들어보자
		
		
		// 아래처럼 쓰면 알아먹질 못한다 왜냐? 자바스크립트의 객체와 자바의 객체가 다르기 때문이다
		// System.out.println(request.getParameter("employee") + "를 저장하자");
		// 그래서json으로 주고받는것임

		// request객체를 통해 넘어온 매개변수를 수집 -> insert 할 수 있는 데이터타입 변환
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");

		// db테이블엔 hireDate가date타입임. 현재는 문자열로 저장되어있으니 변환해야한다
		String tmpHireDate = request.getParameter("hireDate");
		// 문자열 타입의 tmpHireDate를 java.sql.Date타입으로 변환
		Date hireDate = Date.valueOf(tmpHireDate);

		String jobId = request.getParameter("jobId");
		float salary = Float.parseFloat(request.getParameter("salary"));

		// commision은 없을 수도 있음.
		float commissionPct = 0f;
		//String tmpCommissionPct = request.getParameter("commissionPct");

		if (request.getParameter("commissionPct") == null || request.getParameter("commissionPct").equals("")) {
			commissionPct = 0;
		} else {
			commissionPct = Float.parseFloat(request.getParameter("commissionPct"));
			
			//테이블 제약조건 0.01 이런식으로 들어가야 한다
			commissionPct /= 100;
		}

		int managerId = Integer.parseInt(request.getParameter("managerId"));
		int departmentId = Integer.parseInt(request.getParameter("departmentId"));

		
		// dao단으로 매개변수를 전달하기 위해 Employee 객체로 묶자
		// 1)신규 사원 저장을 위해 PK를 먼저 얻어와야 한다. 
		// 2)1번에서 얻어온 PK 값과 아래의 emp 데이터를 함께 insert 해야함 
		// 3)하지만 이곳에서는 트랙잰션 설정을 할 수 없다. Connection 객체가 없다.)
		// 4)그래서 Jsp에서는 아래의 방법으로 처리한다
		Employees emp = new Employees(-1, firstName, lastName, email, phoneNumber, hireDate, jobId, salary,
				commissionPct, managerId, departmentId, null);
		
		//System.out.println(emp);
		
		try {
			//사원이 잘 저장되었을 경우
			//반환할 데이터타입이 json이니까 json 만들자
			boolean result = EmployeesCRUD.getInstance().insertEmployeesTransaction(emp);
			String json = new ResponseJson().makeJsonStringIsSuccess(result);
			
			System.out.println(json);
			
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
