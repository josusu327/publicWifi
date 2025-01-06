<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="Service.WifiService"%>
<%@ page import="model.WifiData"%>
<%@ page import="model.History"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
</head>
<body>
	<div class="container">
		<h1>위치 히스토리 목록</h1>
		<div class="nav-links">
			<a href="index.jsp">홈</a>
			<a href="history.jsp">위치 히스토리 목록</a>
			<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
		</div>
		<form>
			LNT : <input type="text" placeholder="LNT: 0.0" name="lnt" id="lnt" value="<%= request.getParameter("lnt") != null ? request.getParameter("lnt") : "" %>" readonly>
			LAT : <input type="text" placeholder="LAT: 0.0" name="lat" id="lat" value="<%= request.getParameter("lat") != null ? request.getParameter("lat") : "" %>" readonly> 
			<button type="button" onclick="getLocation();">내 위치 가져오기</button>
			<button type="submit">근처 WIFI 정보 보기</button>
		</form>

		<%
    // 히스토리 목록 가져오기
    List<History> historyList = WifiService.getHistoryList();
%>
<h2>히스토리 목록</h2>
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>X좌표</th>
            <th>Y좌표</th>
            <th>조회일자</th>
            <th>비고</th>
        </tr>
    </thead>
    <tbody>
        <%
            if (historyList != null && !historyList.isEmpty()) {
                for (History history : historyList) {
        %>
        <tr>
            <td><%= history.getId() %></td>
            <td><%= history.getLnt() %></td>
            <td><%= history.getLat() %></td>
            <td><%= history.getCreatedTime() %></td>
            <td><button onclick="deleteHistory(<%= history.getId() %>)">삭제</button></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="5">저장된 히스토리가 없습니다.</td>
        </tr>
        <%
            }
        %>
    </tbody>
</table>

	</div>
</body>

<script>

//히스토리 삭제 함수
function deleteHistory(historyId) {
	if (confirm("정말로 삭제하시겠습니까?")) {
		// 서버로 삭제 요청을 보내는 AJAX 요청
		fetch('deleteHistory.jsp?id=' + historyId)
			.then(response => response.text())
			.then(data => {
				alert(data);
				location.reload(); // 페이지 새로 고침 (삭제된 내용 반영)
			})
			.catch(error => console.error('Error:', error));
	}
}

    // 사용자의 위치를 가져오는 함수
    function getLocation() {
        if (navigator.geolocation) {
            // 위치 요청
            navigator.geolocation.getCurrentPosition(showPosition, showError);
        } else {
            alert("이 브라우저는 Geolocation을 지원하지 않습니다.");
        }
    }

    // 위치를 성공적으로 가져왔을 때 실행되는 함수
    function showPosition(position) {
        const lnt = position.coords.longitude;  // 경도-->x
        const lat = position.coords.latitude;  // 위도-->y

        // LAT와 LNT 필드에 값 채우기
        document.getElementById("lnt").value = lnt;
        document.getElementById("lat").value = lat;
       
        // 위치 정보를 서버로 전송하여 DB에 저장
        fetch("list.jsp?lat=" + lat + "&lnt=" + lnt)
            .then(response => response.text())
            .then(data => console.log(data)); // 서버 응답 출력 (디버깅용)
    }

    // 위치를 가져오는 데 실패했을 때 실행되는 함수
    function showError(error) {
        switch (error.code) {
            case error.PERMISSION_DENIED:
                alert("위치 정보 제공이 거부되었습니다.");
                break;
            case error.POSITION_UNAVAILABLE:
                alert("위치 정보를 사용할 수 없습니다.");
                break;
            case error.TIMEOUT:
                alert("위치 요청 시간이 초과되었습니다.");
                break;
            case error.UNKNOWN_ERROR:
                alert("알 수 없는 오류가 발생했습니다.");
                break;
        }
    }
</script>
</html>
