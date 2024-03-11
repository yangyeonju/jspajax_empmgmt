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

@WebServlet("/getEntireEmployees.do")
public class GetEntireEmployees extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public GetEntireEmployees() {
        super();
        // TODO Auto-generated constructor stub
    }

    //요청을 받고 응답을 하는 일만 하는 곳
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//매개변수 가져올것이 없음.
		//System.out.println("!");
		//selectEntireEmployees()하기전에 매개변수먼저 수집
		String orderMethod = request.getParameter("orderMethod");
		System.out.println(orderMethod + "정렬로 정렬하여 전체 사원 데이터 출력하자");
		
		SelectEmployees dao = SelectEmployees.getInstance(); //dao단 메서드 selectEntireEmployees() 호출
		//여기는 서블릿이라 response 응답. 가능! 예외처리를 더이상 미루지 않아도 된다. 
		try {
			
			List<Employees> lst = dao.selectEntireEmployees(orderMethod);
			
//			for(Employees e :lst) {
//				System.out.println(e.toString());
//			}
			
			//얘를 json으로 만들어서 index.jsp로 보내자.
			//json으로 응답하기 위해 
			//ResponseJson은 한번 쓰고 말꺼라 지역변수로 만들필요없이 바로 그냥 아래처럼 쓰는게 낫다.
			String json = new ResponseJson().makeJsonStringEntireEmployeesData(lst);
//			System.out.println(json);
			
			//이제 json 문자열을 응답(response)해주어야한다.
			//json 문자파일 html 문자파일
			//보내가 전에 데이터타입, 인코딩 방식을 설정해준다. index.jsp에서 json으로 달라고했으니까 json으로 주어야한다.
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(json);
//			
			out.flush(); // 버퍼 비우기. 버퍼가 딱 맞아떨어지지 않는다. 큰컵에 담긴 물을 작은컵(버퍼)가 나르는데 그게 딱 맞아떨어지지 않음
			out.close();
			
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
