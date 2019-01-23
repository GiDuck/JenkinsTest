<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false" contentType="text/html; charset=UTF-8"%>

<html>
<head>
<title>★★★다와푸드 오늘의 식단★★★</title>

<style>
body {
	overflow: hidden;
}

/* Preloader */
#preloader {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: #fff;
	/* change if the mask should have another color then white */
	z-index: 99;
	/* makes sure it stays on top */
}

#status {
	width: 200px;
	height: 200px;
	position: absolute;
	left: 50%;
	/* centers the loading animation horizontally one the screen */
	top: 50%;
	/* centers the loading animation vertically one the screen */
	background-image:
		url(https://raw.githubusercontent.com/niklausgerber/PreLoadMe/master/img/status.gif);
	/* path to your loading animation */
	background-repeat: no-repeat;
	background-position: center;
	margin: -100px 0 0 -100px;
	/* is width and height divided by two */
}
</style>


</head>

<script
	src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<body>

	<div id="preloader">
		<div id="status">&nbsp;</div>
	</div>


	<h1>
		★★★다와푸드 오늘의 식단★★★<br> 더이상 귀찮게 앱에 들어가지 말고 여기서 확인하세요..<br> <span
			id="today"></span>

	</h1>


	<h2>중식</h2>
	<ul id="lunchField"></ul>

	<br>
	<br>

	<h2>저녁</h2>
	<ul id="dinnerField"></ul>

	<br>
	<br>


	<div style="width: 100%">

		<input name="beforeDay" type="button" value="< 이전날 " width="30%">
		<input name="afterDay" type="button" value="다음날  >" width="30%">


	</div>



</body>



</html>


<script>
	var todayStr;
	var nowSelectedDay = new Date();

	var Preloader = function() {

		this.on = function() {
			$('#status').fadeIn(); // will first fade out the loading animation 
			$('#preloader').fadeIn(); // will fade out the white DIV that covers the website. 

		},

		this.off = function() {

			$('#status').fadeOut(); // will first fade out the loading animation 
			$('#preloader').fadeOut(); // will fade out the white DIV that covers the website. 
			$('body').css({
				'overflow' : 'visible'
			});

		}

	}

	
	
	var preloaderObj = new Preloader();
	
	function dateFormatter(date) {

		let dateStr;
		let year = date.getFullYear();
		let month = (date.getMonth() + 1) > 9 ? date.getMonth() + 1 : "0" + (date.getMonth() + 1);
		let day = (date.getDate() + 1) > 9 ? date.getDate() : "0" + (date.getDate());
		dateStr = year + "-" + month + "-" + day;

		return dateStr;

	}

	function getMeal(paramDate) {

		preloaderObj.on();

		$.ajax({

			url : "/jenkinsTest/search",
			type : "GET",
			data : {
				date : paramDate.getTime()
			},
			success : function(response) {

				let lunchArr = JSON.parse(response["lunch"]);
				let dinnerArr = JSON.parse(response["dinner"]);
				selectedDateStr = dateFormatter(paramDate);
				selectedDateStr += (" " + paramDate.getDayToKR());

				$("#today").html(selectedDateStr);

				renderScreen(lunchArr, dinnerArr);

			},
			error : function(xhs, status, error) {

				console.log("데이터를 받아오는데 실패하였습니다.. status.. " + status);
				alert("Data load fail");

			}

		})

	}

	function renderScreen(lunchArr, dinnerArr) {

		let $lunchField = $("#lunchField");
		let $dinnerField = $("#dinnerField");

		(function() {

			$lunchField.empty();
			for (let i = 0; i < lunchArr.length; ++i) {

				$lunchField.append($("<li>").html(lunchArr[i]));

			}

		})();

		(function() {

			$dinnerField.empty();
			for (let i = 0; i < dinnerArr.length; ++i) {
				$dinnerField.append($("<li>").html(dinnerArr[i]));
			

			}
			
			preloaderObj.off();
			

		})();

	}

	$(document).ready(function() {

		Date.prototype.weekdayKR = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
		Date.prototype.getDayToKR = function(){
			return this.weekdayKR[this.getDay()];
			
		};
		
		today = new Date();
		todayStr = dateFormatter(today);
		todayStr += (" " + today.getDayToKR());
		$("#today").html(todayStr);
		getMeal(today);

		$("input[name='afterDay']").on("click", function() {

			nowSelectedDay.setDate(nowSelectedDay.getDate() + 1);
			getMeal(nowSelectedDay);

		});

		$("input[name='beforeDay']").on("click", function() {

			nowSelectedDay.setDate(nowSelectedDay.getDate() - 1)
			getMeal(nowSelectedDay);

		});

	});
</script>
