<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>디와푸드 오늘의 식단</title>
</head>

<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<body>
<h1>
	더이상 귀찮게 앱을 들어가지 말고 여기서 확인하세요..<br>
	<span id="today"></span>
	
</h1>


<h2>중식</h2>
<ul id="lunch">


</ul>

<br><br>

<h2>저녁</h2>
<ul id="dinner">


</ul>

<br><br>


<div style="width:100%">

<input type="button" value="다음날 >" width="30%">


</div>



</body>



</html>


<script>

var todayStr;

function initTodayStr(){
	
	
	let date = new Date();
	let year = date.getFullYear();
	let month = date.getMonth()+1 > 9 ? date.getMonth()+1 : "0"+(date.getMonth()+1);
	let day =  date.getDate()+1 > 9 ? date.getDate() : "0"+(date.getDate());
	todayStr = year + "-" + month + "-" + day;
	
}

function getMeal(dateStr){
	
	console.log("datastr.." + dateStr);
	let host = "http://www.dreamforone.com/~wy/api/get_data.php";
	let param = new Object();
	param.store = "dawa";
	param.new = 1;
	param.selectday = dateStr;
	//selectday=2019-01-15&store=dawa&new=1
	
	$.ajax({
		
		url : host,
		type : "GET",
		data : param,
		success : function(response){
			
			console.log(response);
			
			
		},
		error : function(xhs, status, error){
			
			alert("데이터를 받아오는데 실패하였습니다.. status.. " + status);
			
		}
		
	})
	
	
}


$(document).ready(function(){
	
	
	initTodayStr();
	$("#today").html(todayStr);
	getMeal(todayStr);
	


	
});


</script>
