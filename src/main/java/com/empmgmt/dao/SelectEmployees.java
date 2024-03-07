package com.empmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.empmgmt.vo.Employees;

//사원 데이터를 조회하는 기능이 있는 클래스
//이 기능들은 모두가 공유해서 써야하므로 싱글톤으로 만든다.
public class SelectEmployees {

	private static SelectEmployees instance = null;

	// 싱글톤 패턴에서는 외부에서 기본생성자에 접근하지 못하도록 만들어야 한다.
	// 기본생성자를 안만들면 기본으로 만들어지는데 public하게 만들어지므로, private한 생성자가 필요하다면 이렇게 직접 만들어주어야한다.
	private SelectEmployees() {
	}

	public static SelectEmployees getInstance() {
		if (instance == null) {
			instance = new SelectEmployees();
		}
		return instance;
	}

	// =====================================================================
	// 전체 사원 데이터 조회하는 기능
	// GetEntireEmployees.java와는 패키지도 다르고 부모관계도 아님. 그래서 public으로 만든다.
	public List<Employees> selectEntireEmployees() throws NamingException, SQLException {
		// select * from employees 쿼리문을 실행한 후 결과를 반환해줘야 한다.
		// 서블릿으로 보내려면 한꺼번에 담아서 보내야한다. - 사원 전체 리스트를 담을 객체
		List<Employees> empList = new ArrayList<Employees>();

		// 1.DB 연결
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); // -스트림객체

		// 2.쿼리문 준비
		String query = "select e.*, d.department_name " + "from employees e, departments d "
				+ "where e.department_id = d.department_id order by employee_id";

		// 3.PrepareStatement 객체 : 연결된 db에 쿼리문 전송하고 실행하고 결과를 얻어옴 -스트림객체
		PreparedStatement pstmt = con.prepareStatement(query);

		// 4.쿼리문 실행
		// 5.ResultSet에 결과 담기 -스트림객체
		ResultSet rs = pstmt.executeQuery();

		// 6.5번의 결과를 반환할 준비
		while (rs.next()) {
			empList.add(new Employees(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
					rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"),
					rs.getString("JOB_ID"), rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"),
					rs.getInt("MANAGER_ID"), rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME")));
		}

		// 여기까지 잘 나오는지 확인
//		for(Employees e :empList) {
//			System.out.println(e.toString());
//		} 

		// 7.사용했던 스트림객체를 닫고(마지막에 열었던 객체부터 닫기) 6번의 결과를 반환한다.
		db.dbClose(rs, pstmt, con);

		return empList;
	}

	// ====================================================
	// 이름으로 사원 데이터를 조회하는 기능
	// 이름이 포함된 사원을 검색하는 메서드
	// 0명나올수도있고1명나올수도있고많이나올수도있다
	public List<Employees> selectEmployeesBySearchName(String searchName) throws NamingException, SQLException {

		List<Employees> empList = new ArrayList<Employees>();

		// DB연결
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); // -스트림객체

		// 2.쿼리문 준비
		String query = "select e.*, d.department_name " + "from employees e, departments d "
				+ "where e.department_id = d.department_id " + "and e.first_name like ?";

		// 3.PrepareStatement 객체 : 연결된 db에 쿼리문 전송하고 실행하고 결과를 얻어옴 -스트림객체
		PreparedStatement pstmt = con.prepareStatement(query);

		// 실행하기전에 매개변수 세팅
		pstmt.setString(1, "%" + searchName + "%");

		// 4.쿼리문 실행
		// 5.ResultSet에 결과 담기 -스트림객체
		ResultSet rs = pstmt.executeQuery();

		// 6.5번의 결과를 반환할 준비
		while (rs.next()) {
			empList.add(new Employees(rs.getInt("EMPLOYEE_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
					rs.getString("EMAIL"), rs.getString("PHONE_NUMBER"), rs.getDate("HIRE_DATE"),
					rs.getString("JOB_ID"), rs.getFloat("SALARY"), rs.getFloat("COMMISSION_PCT"),
					rs.getInt("MANAGER_ID"), rs.getInt("DEPARTMENT_ID"), rs.getString("DEPARTMENT_NAME")));
		}
		
//		for (Employees e : empList) {
//			System.out.println(e.toString());
//		}
		
		// 7.사용했던 스트림객체를 닫고(마지막에 열었던 객체부터 닫기) 6번의 결과를 반환한다.
		db.dbClose(rs, pstmt, con);
		
		return empList;

	}

	// 사번으로 사원 데이터 조회하는 기능

}
