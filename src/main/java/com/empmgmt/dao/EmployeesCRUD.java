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
public class EmployeesCRUD {

	private static EmployeesCRUD instance = null;

	// 싱글톤 패턴에서는 외부에서 기본생성자에 접근하지 못하도록 만들어야 한다.
	// 기본생성자를 안만들면 기본으로 만들어지는데 public하게 만들어지므로, private한 생성자가 필요하다면 이렇게 직접 만들어주어야한다.
	private EmployeesCRUD() {
	}

	public static EmployeesCRUD getInstance() {
		if (instance == null) {
			instance = new EmployeesCRUD();
		}
		return instance;
	}

	// =====================================================================
	// 전체 사원 데이터 조회하는 기능
	// GetEntireEmployees.java와는 패키지도 다르고 부모관계도 아님. 그래서 public으로 만든다.
	public List<Employees> selectEntireEmployees(String orderMethod) throws NamingException, SQLException {
		// select * from employees 쿼리문을 실행한 후 결과를 반환해줘야 한다.
		// 서블릿으로 보내려면 한꺼번에 담아서 보내야한다. - 사원 전체 리스트를 담을 객체
		List<Employees> empList = new ArrayList<Employees>();

		// 1.DB 연결
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); // -스트림객체

		// 2.쿼리문 준비
		//넘겨지는 매개변수 orderMethod의 값에 따라 쿼리문이 달라진다 => 동적 SQL
		String query = "select e.*, d.department_name " + "from employees e, departments d "
				+ "where e.department_id = d.department_id order by ";
		
		if(orderMethod.equals("empNo")) {
			 query += " employee_id";
		} else if (orderMethod.equals("salary")) {
			query += " salary desc";
		} else if (orderMethod.equals("hireDate")) {
			query += " hire_date desc";
		}

		// 3.PrepareStatement 객체 : 연결된 db에 쿼리문 전송하고 실행하고 결과를 얻어옴 -스트림객체
		PreparedStatement pstmt = con.prepareStatement(query);

		// 4.쿼리문 실행
		// 5.ResultSet에 결과 담기 -스트림객체
		ResultSet rs = pstmt.executeQuery();

		// 6.5번의 결과를 반환할 준비
		//아래의 Employee 는 vo
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
	//이름과 성에 searchName이 포하된 사원을 검색하는 메서드
	public List<Employees> selectEmployeesBySearchName(String searchName) throws NamingException, SQLException {

		List<Employees> empList = new ArrayList<Employees>();

		// DB연결
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); // -스트림객체

		// 2.쿼리문 준비
		String query = "select e.*, d.department_name "
				+ "from employees e, departments d "
				+ "where e.department_id = d.department_id "
				+ "and (lower(e.first_name) like lower(?) or lower(e.last_name) like lower(?)) "
				+ "order by employee_id";

		// 3.PrepareStatement 객체 : 연결된 db에 쿼리문 전송하고 실행하고 결과를 얻어옴 -스트림객체
		PreparedStatement pstmt = con.prepareStatement(query);

		// 실행하기전에 매개변수 세팅
		pstmt.setString(1, "%" + searchName + "%");
		pstmt.setString(2, "%" + searchName + "%");

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

	// ====================================================
	// 매개변수로 받은 email 값이 중복인지 아닌지를 검사하는 메서드
	// 매개변수 : String email (유저가 입력한 이메일)
	// 반환값 : boolean (true : 중복된다, false: 중복 아니다)
	public boolean isDuplicateEmail(String email) throws NamingException, SQLException {
		
		boolean result = false;
		
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); // -스트림객체
		
		String query = "select * from employees where email=?";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1, email);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			//결과 행이(row)가 null이 아니라면 -> 중복된 이메일이다.
			result = true;
		}
		
		db.dbClose(rs, pstmt, con);
		
		return result;
		
	}
	
	
	// ====================================================
	// 신규사원저장을 위한 전체적인 트랜잭션을 제어하는 메서드
	// 사원이 잘 저장되었다면 true 저장되지 않았다면 false를 반환하자
	public boolean insertEmployeesTransaction(Employees emp) throws NamingException, SQLException {
		
		boolean result = false;
		
		//1)DB연결
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); //db Open
		
		//2)트랜잭션 작업을 위한 설정
		//autoCommit되지 않게 설정한다. (내가 직접 commit/rollback명령어를 수행하겠다는 의미)
		con.setAutoCommit(false);
		
		int empNo = -1;
		//트랜잭션은 반드시 하나의 Connection 가지고 놀아야 한다 그래서 지역변수인 con을 매개변수로 전달
		//3)신규 사원 저장을 위해 PK를 먼저 얻어와야 한다. select
		empNo = getNextEmployeeId(con);
		
		//4)3번에서 얻어온 PK 값과 아래의 emp 데이터를 함께 insert
		emp.setEmployeeId(empNo); //얻어온 employee_id값을 emp 객체에 저장
		
		 if (empNo != -1) {
			//5)모든 트랜잭션 작업이 끝나면 commit이나 rollback을 수행
			//위의 3번 작업이 정상 수행되었을 때
			//empNo가 -1이 아닐때 정상적으로 얻어온 것 
			if(insertEmployee(con, emp) == 1) {
				//1행이 정상적으로 삽입되었을 때
				con.commit(); // select + insert 작업이 모두 수행 되었을 때
				result = true;
			}else {
				con.rollback(); // insert 작업이 수행되지 않았을 때
			}
		}
		
		//6)위에서 열었던 db를 close
		 con.close();
		
		 return result;
	}

	//4)3번에서 얻어온 PK 값과 아래의 emp 데이터를 함께 insert
	//실제 emp를 연결된 데이터베이스 con에 저장하고 결과를 반환하는 메서드
	private int insertEmployee(Connection con, Employees emp) throws SQLException {
		String query = "insert into employees values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		//PreparedStatement객체 준비
		PreparedStatement pstmt = con.prepareStatement(query);
		
		System.out.println(emp.toString());
		//매개변수 세팅
		pstmt.setInt(1, emp.getEmployeeId());
		pstmt.setString(2, emp.getFirstName());
		pstmt.setString(3, emp.getLastName());
		pstmt.setString(4, emp.getEmail());
		pstmt.setString(5, emp.getPhoneNumber());
		pstmt.setDate(6, emp.getHireDate());
		pstmt.setString(7, emp.getJobId());
		pstmt.setFloat(8, emp.getSalary());
		pstmt.setFloat(9, emp.getCommissionPct());
		pstmt.setInt(10, emp.getManagerId());
		pstmt.setInt(11, emp.getDepartmentId());
		
		//쿼리문 실행
		int result = pstmt.executeUpdate();
		
		pstmt.close();
		
		return result;
		
	}

	//3) 신규 사원 저장을 위해 PK 얻어오는 메서드
	private int getNextEmployeeId(Connection con) throws SQLException {
		
		int nextEmpNo = -1; //반환할변수
		
		//쿼리문 준비
		String query = "select max(employee_id)+1 as nextEmpNo from employees";
		
		//PreparedStatement객체 준비
		PreparedStatement pstmt = con.prepareStatement(query);
		
		//쿼리문 실행
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			nextEmpNo = rs.getInt("nextEmpNo");
		}
		
		DBConnection.getInstance().dbClose(rs, pstmt);
		
		return nextEmpNo;
	}
	
	// ====================================================
	//사원을 삭제하는 메서드
	//반환값 삭제 성공이면true실패면 false
	public boolean deleteEmployee(int empNo) throws NamingException, SQLException {
		
		boolean result = false;
		
		DBConnection db = DBConnection.getInstance();
		Connection con = db.dbOpen(); // -스트림객체
		
		String query = "delete from employees where employee_id = ?";
		
		PreparedStatement pstmt = con.prepareStatement(query);
		
		pstmt.setInt(1, empNo);
		
		if( pstmt.executeUpdate() == 1 ) {
			result = true;
		}
		
		db.dbClose(pstmt, con);
		
		return result;
		
	}
	
	
	
	// 사번으로 사원 데이터 조회하는 기능

}
