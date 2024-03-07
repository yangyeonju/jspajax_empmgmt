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


   $(function (){  // 자바에서 main()함수의 기능 (현재 페이지의 태그가 로드되면 자동으로 호출되는 함수)
      // 현재 페이지가 로딩되면 전체 사원목록을 얻어와 출력
      getEntireEmployeesData();      
      
      
   });
   
   function getEntireEmployeesData() {
      // 서블릿에게 전체 사원데이터를 달라고 요청 (ajax : 비동기데이터 통신)
        $.ajax({
          url: './getEntireEmployees.do', // 데이터를 송수신할 서버의 주소 (서블릿 매핑주소)
          type: 'get', // 통신방식(GET, POST, PUT, DELETE)
          dataType: 'json', // 수신받을 데이터의 타입
          success: function (data) {  // data(json)
            // 통신 성공하면 실행할 내용들....
            console.log(data);
             
             outputEntireEmployees(data);

          }
        });
   }
   
   function outputEntireEmployees(data) {
      let output = '';
      output += "<table class='table table-hover'><thead>";
      output += "<tr><th>순번</th><th>사번</th><th>이름</th><th>Email</th><th>전화번호</th><th>입사일</th>";
      output += "<th>직급</th><th>급여</th><th>커미션</th><th>사수</th><th>소속부서</th></tr></thead>";
      output += "<tbody>";
      
      // 사원수만큼 반복하여 출력
      $.each(data.employees, function(i, e) {
         console.log($(e));
         output += "<tr>";
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
         if(comm != 0){
	         output += `<td>\${comm}%</td>`;        	 
         }else{
        	 output += `<td>-</td>`;
         }
         
         
         
         output += `<td>\${e.manager_id}</td>`;
         output += `<td>\${e.department_id}</td>`;
         output += "</tr>";
      });
      
       output += "</tbody></table>";
       
       $(".outputData").html(output);
   }
   
   
</script>
</head>
<body>
   <div class="container-fluid p-5 bg-primary text-white text-center">
      <h1>Employee Management</h1>
      <p>ver 1.0</p>
   </div>

   <div class="outputData"></div>
</body>
</html>