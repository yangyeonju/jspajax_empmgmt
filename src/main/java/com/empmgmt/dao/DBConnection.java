package com.empmgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnection {
	
	private static DBConnection instance = null;
	
	//싱글톤 패턴에서는 외부에서 기본생성자에 접근하지 못하도록 만들어야 한다.
	//기본생성자를 안만들면 기본으로 만들어지는데 public하게 만들어지므로, private한 생성자가 필요하다면 이렇게 직접 만들어주어야한다.
	private DBConnection() { }

	public static DBConnection getInstance() {
		if(instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}
	
	//실제 오라클 데이터베이스에 접속하는 기능의 메서드
	//throws : 현재 메서드를 호출한 곳에서 예외 처리 하도록 미뤄둔 것
	public Connection dbOpen() throws NamingException, SQLException {
		//InitialContext 초기 컨텍스트를 구성합니다. 환경 프로퍼티는 제공되지 않습니다. 새로운 InitialContext(null)와 동일합니다.
		Context initContext = new InitialContext(); //context.xml 파일의 객체
		
		//웹 서버에서 context.xml 파일의 환경설정부분에서 내용을 읽어와 envContext에 저장
		Context envContext  = (Context)initContext.lookup("java:/comp/env");
		
		//context.xml파일의 내용에서 "jdbc/webyyj"를(오라클 데이터베이스 접속정보) 검색해 찾아서 ds에 저장
		//DataSurce 이 DataSource 객체가 나타내는 물리적 데이터 소스에 대한 연결을 위한 팩토리입니다.
		DataSource ds = (DataSource)envContext.lookup("jdbc/webyyj");
		
		Connection conn = ds.getConnection(); //DataSource(DBCP)에서 Connection 객체 얻어옴
		
		return conn;
	}
	
	public void dbClose(ResultSet rs, PreparedStatement pstmt, Connection con) throws SQLException {
		rs.close();
		pstmt.close();
		con.close();
	}
	
	public void dbClose(ResultSet rs, PreparedStatement pstmt) throws SQLException {
		rs.close();
		pstmt.close();
	}
	
	
}
