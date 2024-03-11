<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<title>Insert title here</title>
<script>
	let empData = null; //전체 사원 목록
	let jobsInfo = null; //jobs 정보 목록
	let deptData = null; //전체 부서 목록

	$(function() { // 자바에서 main()함수의 기능 (현재 페이지의 태그가 로드되면 자동으로 호출되는 함수)
		// 현재 페이지가 로딩되면 전체 사원목록을 얻어와 출력
		getEntireEmployeesData();

		//keyup이 되면 evt가 실행된다.
		$(".searchName").keyup(function(evt) {
			//.searchName (input)의 value값을 변수에 넣어줌.
			let searchName = $(this).val();

			//1글자만 입력했을때 실행되는게 아니라 3글자입력했을때부터 찾도록 할 예정.
			//검색할 사원의 이름을입력받는곳에 문자열이 입력되고 그 문자열의 길이가 3이상이면
			if (searchName.length >= 2) {
				getEmployeesByName(searchName);
			}

			if (searchName.length == 0) {
				//검색어가 없다면
				outputEntireEmployees();
			}
		});

		//라디오 버튼을 클릭하면 다음의 함수 실행
		$(".orderMethod").click(function() {
			//정렬 라디오 버튼을 클릭하면 클릭한 라디오 버튼의 value값을 얻어옴
			//console.log($(this).val());
			//orderMethod = 정렬하는 방
			let orderMethod = $(this).val();
			//정렬만 바뀌는거지 전체데이터를 얻어오는 것은 똑같다. 정렬의 값을 보내면서 전체데이터 출력함수 호출
			getEntireEmployeesData(orderMethod);

		});

		//saveEmpModalClose 버튼을 클릭하면 신규사원저장 모달을 닫아야 한다
		$('.saveEmpModalClose').click(function() {
			$('#saveEmpModal').hide();
		});

		//유저가 사원을 신규 저장시 직급을 선택했다면..
		$('#saveJobIdSelectTag').change(function() {
			//alert("!");
			let tmpSelected = $(this).val();
			if (tmpSelected == -1) {
				//직급이 선택되지 않았을 때
				alert("직급을 선택하세요");
			} else {
				//직급을 선택해야 해당 직급의 최소/최대 급여를 알 수 있다.
				//이 시점에서 Input type range 만들 수 있다..
				makeSalaryRangeTag(tmpSelected);
			}
		});

		//급여 슬라이딩 값이 변하면 선택된 급여가 출력되도록 한다
		/* $("#salary").change(function() {
			alert("!");
			let sal = $(this).val();
			$(".selectedSalary").html(sal);
		}) */

		//동적으로 만들었을 땐 onchange 걸자
		$('.rangeSalaryTag').on('change', 'input', function(evt) {
			$(".selectedSalary").html(evt.target.value);
		})

	});

	//인풋타입range로 태그를 만드는 함수 (급여입력용)
	function makeSalaryRangeTag(selectedJobId) {
		//alert(selectedJobId); // 배열이므로 반복문 돌려서 selectedJobId를 찾아내야한다.
		let minSal = 0, maxSal = 0;
		let jobs = jobsInfo.jobs;

		$.each(jobs, function(i, e) {
			if (selectedJobId == e.job_id) {
				//선택된 직급이 배열에서와 직급이랑 같은걸 찾아야 한다
				minSal = e.min_salary;
				maxSal = e.max_salary;

			}
		})

		let output = `<input type="range" class="form-range" min="\${minSal}" max="\${maxSal}" step="100" id="salary">`;
		$('.saveMinSal').html(minSal);
		$('.saveMaxSal').html(maxSal);

		$('.rangeSalaryTag').html(output);

	}

	function getEmployeesByName(searchName) {
		// 서블릿에게 이름에 searchName이 포함된 사원데이터를 달라고 요청 (ajax : 비동기데이터 통신)
		$.ajax({
			url : './getEmployeeByName.do', // 데이터를 송수신할 서버의 주소 (서블릿 매핑주소)
			type : 'get', // 통신방식(GET, POST, PUT, DELETE)
			dataType : 'json', // 수신받을 데이터의 타입
			//서블릿으로 전송하는 데이터
			data : {
				"searchName" : searchName
			},
			success : function(data) { // data(json)
				// 통신 성공하면 실행할 내용들....
				console.log(data);

				outputEntireEmployees(data);
			}
		});
	}

	function getEntireEmployeesData(orderMethod) {
		//orderMethod가 있을수도 없을수도 있다
		if (orderMethod == null) {
			//정렬기능 라디오 버튼을 클릭하지 않았다면 (제일 처음 수행될 때) 사번으로 정렬되도록 한다
			orderMethod = 'empNo';
		}

		console.log(orderMethod);

		// 서블릿에게 전체 사원데이터를 달라고 요청 (ajax : 비동기데이터 통신)
		$.ajax({
			url : './getEntireEmployees.do', // 데이터를 송수신할 서버의 주소 (서블릿 매핑주소)
			type : 'get', // 통신방식(GET, POST, PUT, DELETE)
			dataType : 'json', // 수신받을 데이터의 타입
			data : {
				//서블렛에게 보낼 orderMethod 데이터를 객체로 보낸다.
				"orderMethod" : orderMethod
			},
			success : function(data) { // data(json)
				// 통신 성공하면 실행할 내용들....
				console.log(data);
				//전역변수에 data 넣기
				empData = data;
				outputEntireEmployees();

			}
		});
	}

	function outputEntireEmployees(data) {
		let output = '';
		let employees = null;

		//메서드 오버로딩이 없어서 이렇게 만들어야 한다.
		if (data != null) {
			//이름으로 검색할 때
			//data.employees 가 배열
			//넘겨받은 데이터로출력
			employees = data.employees;
		} else {
			//전체 사원으로 검색할 때
			//전역변수 데이터로 출력
			employees = empData.employees;
		}

		output += "<table class='table table-hover'><thead>";
		output += "<tr><th>순번</th><th>사번</th><th>이름</th><th>Email</th><th>전화번호</th><th>입사일</th>";
		output += "<th>직급</th><th>급여</th><th>커미션</th><th>사수</th><th>소속부서</th></tr></thead>";
		output += "<tbody>";

		// 사원수만큼 반복하여 출력
		//data는 json 그자체이고 data.employees 해야 배열이 나옴
		$.each(employees, function(i, e) {
			//i는 몇번째, $(e)는 사원 정보
			//console.log($(e));
			output += "<tr>";

			//백틱 쓸 때 달라 중괄호는 jsp에서 el표현식이랑 겹치기떄문에 escape \ 써주어야함
			//데이터를 java에서 보내면 jsp에서 알아서 el로 알아들음.
			//하지만 지금은 ajax. json으로 만들어 js로 보내왔기 때문에 이 데이터는 더이상 java 객체가 아니다.
			//그러므로 이 경우 js! el과 jstl표현식을 쓰지 못한다. 

			output += `<td>\${i + 1}</td>`;
			output += `<td>\${e.employee_id}`;
			output += `<td>\${e.first_name}, \${e.last_name}</td>`;
			output += `<td>\${e.email}</td>`;
			output += `<td>\${e.phone_number}</td>`;
			output += `<td>\${e.hire_date}</td>`;
			output += `<td>\${e.job_id}</td>`;
			//sal이 숫자같지만.. 제이슨으로 가져왔으면 문자열이니까. 여기선 출력할게 아니니까. 중괄호 쓰면 안된다.
			//급여가 금액이므로 단위($) 붙이고 3자리마다 콤마를 찍어 표현
			let sal = Number(e.salary).toLocaleString();
			output += `<td>$\${sal}</td>`;

			//커미션은 %단위이므로 *100을 하고 단위를 붙여 표현
			let comm = Number(e.commission_pct) * 100;
			if (comm != 0) {
				output += `<td>\${comm}%</td>`;
			} else {
				output += `<td>-</td>`;
			}

			//직속상사의 사번이 아닌 이름을 출력하자
			let managerId = e.manager_id;
			let managerName = findManagerName(managerId);
			output += `<td>\${managerName}</td>`;

			//output += `<td>\${e.department_id}</td>`;
			output += `<td>\${e.department_name}</td>`;
			output += "</tr>";
		});

		output += "</tbody></table>";

		$(".outputData").html(output);
	}

	function findManagerName(managerId) {
		//managerId: 이름을 찾을 직속상사의 사번
		//전체사원목록이 있는 배열을 반복하면서 사번이 managerId인 사원의 이름을 찾아 반환한다.
		//얘는 break를 못한다.
		let managerName = "";

		$.each(empData.employees, function(i, e) {
			//console.log($(e));
			//직속상사의사번이 사번과 같은 아이를 사원 인원수만큼 반복하며 하나하나 비교하면서 찾는다.

			if (e.employee_id == managerId) {
				managerName = e.first_name + ", " + e.last_name;
			}

		})
		return managerName; //사번이 managerId인 사원의 이름(사수이름)

	}

	//신규사원 저장을 위해 모달창 오픈
	function saveModalOpen() {
		//모달창 보이기 전에 jobs테이블에서 데이터를 가져온다
		getJobsInfo(); //얘가 다 되어야 아래꺼를 호출해야 한다 동기방식으로 하기
		makeJobSelectTag();
		makeManagerSelectTag();
		getDepartmentsInfo();
		makeDeptSelectTag();
		
		//select 박스에 option태그 만들어야 함. 함수로
		$("#saveEmpModal").show(200);
	}
	
	
	function makeDeptSelectTag(){
		
		let output = "<option value='-1'>--- 부서를 선택하세요 ---</option>";
		let departments = deptData.departments; //부서 배열
		
		$.each(departments, function(i, e) {
			output += `<option value='\${e.department_id}'> \${e.department_name} </option>`
		});
		
		$("#saveDepartmentsTag").html(output);
		
	}
	
	
	//부서 정보를 json으로 얻어오는 함수
	function getDepartmentsInfo(){
		$.ajax({
			url : './getDeptInfo.do', // 데이터를 송수신할 서버의 주소 (서블릿 매핑주소)
			type : 'get', // 통신방식(GET, POST, PUT, DELETE)
			dataType : 'json', // 수신받을 데이터의 타입
			async : false,
			success : function(data) { // data(json)
				// 통신 성공하면 실행할 내용들....
				console.log(data);
				deptData = data;
				
			}
		});
	}

	function makeManagerSelectTag(){
		let output = "<option value='-1'>--- 상사를 선택하세요 ---</option>";
		let employees = empData.employees;
		
		$.each(employees, function(i, e) {
			output += `<option value="\${e.employee_id}">\${e.first_name}, \${e.last_name} (\${e.employee_id})</option>`;
		});
		
		$("#saveManagerIdSelectTag").html(output);
	}
	
	
	function makeJobSelectTag() {
		let jobs = jobsInfo.jobs; //json에서 배열만 가져옴

		let output = "<option value='-1'>--- 직급을 선택하세요 ---</option>";

		$.each(jobs, function(i, e) {
			//jobs배열에서 하나씩 가져와서 e에 저장한다
			//console.log($(e));
			output += `<option value='\${e.job_id}'>\${e.job_title }</option>`;
		});

		$("#saveJobIdSelectTag").html(output);

	}

	function getJobsInfo() {
		$.ajax({
			url : './getJobsInfo.do', // 데이터를 송수신할 서버의 주소 (서블릿 매핑주소)
			type : 'get', // 통신방식(GET, POST, PUT, DELETE)
			dataType : 'json', // 수신받을 데이터의 타입
			async : false, //동기 방식 수행
			success : function(data) { // data(json)
				// 통신 성공하면 실행할 내용들....
				console.log(data);
				jobsInfo = data;

			}
		});
	}
</script>
<style>
.outputData, .searchName {
	margin-top: 20px;
}

.searchName input {
	text-align: center;
	font-size: 15px;
}

.sortEmp {
	margin-top: 20px;
	display: flex;
	flex-direction: row;
	justify-content: space-around;
}

.saveIcon {
	position: fixed;
	bottom: 20px;
	right: 20px;
	cursor: pointer;
}

.minSalMaxSal {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}
</style>
</head>
<body>
	<div class="container-fluid p-5 bg-primary text-white text-center">
		<h1>Employee Management</h1>
		<p>ver 1.0</p>
	</div>

	<div class="container">
		<!-- ajax는 form태그를 쓰지 않는다! -->
		<div class="row">
			<div class="col">
				<input type="text" class="form-control searchName"
					placeholder="찾을 사원의 이름을 입력하세요." name="findEmpName">
			</div>
		</div>
		<div class="sortEmp">
			<div class="form-check">
				<input type="radio" class="form-check-input orderMethod" id="radio1"
					name="orderMethod" value="empNo" checked>사번(오름차순) <label
					class="form-check-label" for="radio1"></label>
			</div>
			<div class="form-check">
				<input type="radio" class="form-check-input orderMethod" id="radio2"
					name="orderMethod" value="hireDate">입사일(내림차순) <label
					class="form-check-label" for="radio2"></label>
			</div>
			<div class="form-check">
				<input type="radio" class="form-check-input orderMethod"
					name="orderMethod" value="salary">급여(내림차순) <label
					class="form-check-label"></label>
			</div>
		</div>
	</div>

	<div class="container outputData"></div>

	<!-- 신규 사원을 저장하는 버튼 -->
	<div class="saveIcon">
		<img width="50" height="50"
			src="https://img.icons8.com/ios-filled/50/add--v1.png" alt="add--v1"
			onclick="saveModalOpen();" />
	</div>

	<!-- 신규 사원을 저장하는 모달창 -->
	<div class="modal" id="saveEmpModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- Modal Header -->
				<div class="modal-header">
					<h4 class="modal-title">신규 사원 등록</h4>
					<button type="button" class="btn-close saveEmpModalClose"
						data-bs-dismiss="modal"></button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">
					<div class="mb-3 mt-3">
						<label for="savefirstName" class="form-label">firstName:</label> <input
							type="text" class="form-control" id="savefirstName">
					</div>
					<div class="mb-3 mt-3">
						<label for="savelastName" class="form-label">lastName:</label> <input
							type="text" class="form-control" id="savelastName">
					</div>
					<div class="mb-3 mt-3">
						<label for="saveEmail" class="form-label">email:</label> <input
							type="text" class="form-control" id="saveEmail">
					</div>
					<div class="mb-3 mt-3">
						<label for="savePhoneNumber" class="form-label">phoneNumber:</label>
						<input type="text" class="form-control" id="savePhoneNumber">
					</div>
					<div class="mb-3 mt-3">
						<label for="saveHireDate" class="form-label">hireDate:</label> <input
							type="date" class="form-control" id="saveHireDate" />
					</div>
					<div class="mb-3 mt-3">
						<label for="saveJobId" class="form-label">jobId:</label> <select
							id="saveJobIdSelectTag" class="form-select">
							<!-- 프로그램 시작하면 바로 jobs테이블에서 데이터를 불러오거나, 모달창 플러스 부분 클릭하면 불러온다. -->
						</select>
					</div>
					<div class="mb-3 mt-3">
						<label for="saveSalary" class="form-label">salary: <span
							class="selectedSalary"></span></label>
						<div class="rangeSalaryTag"></div>

						<div class="minSalMaxSal">
							<span class="saveMinSal"></span> <span class="saveMaxSal"></span>
						</div>
					</div>

					<div class="mb-3 mt-3">
						<label for="saveComm" class="form-label">Commission(%):</label> 
						<input type="number" class="form-control" id="saveComm" min="0" max="100" />
					</div>
					
					<div class="mb-3 mt-3">
						<label for="saveManagerId" class="form-label">Manager:</label> 
						<select id="saveManagerIdSelectTag" class="form-select">
							<!-- 프로그램 시작하면 바로 jobs테이블에서 데이터를 불러오거나, 모달창 플러스 부분 클릭하면 불러온다. -->
						</select>
					</div>
					
					<div class="mb-3 mt-3">
						<label for="saveDepartments" class="form-label">Departments:</label> 
						<select id="saveDepartmentsTag" class="form-select">
							<!-- 프로그램 시작하면 바로 jobs테이블에서 데이터를 불러오거나, 모달창 플러스 부분 클릭하면 불러온다. -->
						</select>
					</div>

				</div>

				<!-- Modal footer -->
				<div class="modal-footer">
					<button type="button" class="btn btn-danger saveEmpModalClose"
						data-bs-dismiss="modal">Close</button>
				</div>

			</div>
		</div>
	</div>
</body>
</html>