package com.empmgmt.etc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.empmgmt.vo.Departments;
import com.empmgmt.vo.Employees;
import com.empmgmt.vo.Jobs;

public class ResponseJson {

	//lst(전체사원목록)을 simple json 라이브러리를 이용하여 json 문자열로 만들어 반환한다.
	public String makeJsonStringEntireEmployeesData(List<Employees> lst) {
		//제일 바깥 {} 만들었다.
		JSONObject jsonObj = new JSONObject(); //json 객체 그 자체
		JSONArray employees = new JSONArray(); //사원 객체의 배열이 들어갈 객체 (사원 전체)
		
		for(Employees e : lst) {
			//얘를반복문이 아닌 위로 만들면 한명의 사원 데이터마다 데이터가 저장되어야하는데
			//한 변수에사원데이터가 계속 오버라이트 된다..
			JSONObject employee = new JSONObject(); // 한명의 사원 데이터가 들어가는 객체
			//jsonObject는 HashMap 상속받음. 그래서 employee에 데이터를 더하려면 put을 써야 함.
			//e.getEmployeeId()는 int타입이니까 문자타입으로 만들려면.. 제일빠른 방법은 뒤에 빈 문자열 더해주기.
			employee.put("employee_id", e.getEmployeeId() + "");
			employee.put("first_name", e.getFirstName());
			employee.put("last_name", e.getLastName());
			employee.put("email", e.getEmail());
			employee.put("phone_number", e.getPhoneNumber());
			//getHireDate 는 date 타입이니까 string으로 바꿔주어야한다.
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			//format() 은 DateFormat의 메서드. 날짜를 날짜/시간 문자열로 포맷합니다.
			employee.put("hire_date",  df.format(e.getHireDate()));
			employee.put("job_id", e.getJobId());
			employee.put("salary", e.getSalary() + "");
			employee.put("commission_pct", e.getCommissionPct() + "");
			employee.put("manager_id", e.getManagerId() + "");
			employee.put("department_id", e.getDepartmentId() + "");
			employee.put("department_name", e.getDepartmentName());
			
			//이 데이터들을 까먹기전에 배열에 넣어주어야 함.
			
			//JSONArray도 java.util.List를 상속받아 만든거라 add 메서드가 있다.
			employees.add(employee); //한명의사원을 배열에 저장
		}
		
		jsonObj.put("employees", employees); // jsonObj에(바깥 껍데기) JSONArray를 "employees"라는 이름으로 저장.
		
		//이제 jsonObj를 json문자열로 만들어서 리턴해준다.
		return jsonObj.toJSONString();
		
	}
	
	//List<Jobs>를 받아 simple json 라이브러리를 이용하여 json 문자열로 만들어 반환한다.
	public String makeJsonStringJobsData(List<Jobs> jobList) {
		JSONObject jsonObj = new JSONObject();
		JSONArray jobsArr = new JSONArray();
		
		for(Jobs j : jobList) {
			JSONObject job = new JSONObject();
			job.put("job_id", j.getJobId());
			job.put("job_title", j.getJobTitle());
			job.put("min_salary", j.getMinSalary() + "");
			job.put("max_salary", j.getMaxSalary() + "");
			
			jobsArr.add(job);
		}
		
		jsonObj.put("jobs", jobsArr);
		
		return jsonObj.toJSONString();
	}
	
	//List<Department>를 받아 문자열로 반환하는 메서드
	public String makeJsonStringDeptData(List<Departments> lst) {
		JSONObject jsonObj = new JSONObject();
		JSONArray departments = new JSONArray();
		
		for(Departments d :lst) {
			JSONObject department = new JSONObject();
			department.put("department_id", d.getDepartmentId() + "");
			department.put("department_name", d.getDepartmentName());
			department.put("manager_id", d.getManagerId() + "");
			department.put("location_id", d.getLocationId() + "");
			
			departments.add(department);
		}
		
		jsonObj.put("departments", departments);
		
		return jsonObj.toJSONString();
	}
	
	//boolean result을 받아 중복인지 아닌지 json문자열로 반환하는 메서드
	public String makeJsonStringDuplicateEmail(boolean result) {
		JSONObject jsonObj = new JSONObject();
		
		if(result == true) {
			jsonObj.put("isDuplicate", "true");			
		}else {
			jsonObj.put("isDuplicate", "false");
		}
		
		return jsonObj.toJSONString();
		
	}
	
	
	//boolean result를 받아 저장 성공인지 실패인지 json문자열로 반환하는 메서드
	public String makeJsonStringIsSuccess(boolean result) {
		JSONObject jsonObj = new JSONObject();
		
		if(result == true) {
			jsonObj.put("success", "true");			
		} else {
			jsonObj.put("success", "false");
		}
		
		return jsonObj.toJSONString();
	}
	
	
	
	
}
