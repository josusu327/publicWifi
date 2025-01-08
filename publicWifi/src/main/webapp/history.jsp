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

<style>
body {
	margin: 0;
	padding: 0;
}

.container {
	padding: 10px;
}

.nav-links {
	margin-bottom: 20px;
}

.nav-links a:hover {
	text-decoration: underline;
}

form {
	margin-bottom: 20px;
}

button {
	cursor: pointer;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

table, th, td {
	border: 1px solid #ccc;
}

th {
	background-color: #00AE67;
	color: white;
	padding: 10px;
}

td {
	padding: 8px;
}

tr td:nth-child(5) {
	text-align: center;
	vertical-align: middle;
}

.empty-message {
	color: black;
}
</style>

<body>
	<div class="container">
		<h1>위치 히스토리 목록</h1>
		<div class="nav-links">
			<a href="index.jsp">홈</a>
			<a href="history.jsp">위치 히스토리 목록</a>
			<a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
		</div>
		
		<%
				// 히스토리 목록 가져오기
				List<History> historyList = WifiService.getHistoryList();
				%>
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
            <td><button onclick="deleteHistory(<%= history.getId() %>);">삭제</button></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="5" class="empty-message">저장된 히스토리가 없습니다.</td>
        </tr>
        <%
            }
        %>
    </tbody>
</table>

	</div>
</body>

<script>

    // 히스토리 삭제 함수
    function deleteHistory(historyId) {
        if (confirm("정말로 삭제하시겠습니까?")) {
            // 서버로 삭제 요청을 보내는 fetch 요청
            fetch('history.jsp?action=delete&id=' + historyId)
                .then(response => response.text())
                .then(data => {
                    location.reload(); // 페이지 새로 고침 (삭제된 내용 반영)
                })
                .catch(error => console.error('Error:', error));
        }
    }
</script>
</html>

<%
    // 히스토리 삭제 처리
    String action = request.getParameter("action");
    if ("delete".equals(action)) {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int historyId = Integer.parseInt(idParam);
                WifiService.deleteHistory(historyId);  // 히스토리 삭제 메서드 호출
                out.print("삭제되었습니다.");
            } catch (NumberFormatException e) {
                out.print("유효하지 않은 ID입니다.");
            }
        } else {
            out.print("ID 파라미터가 없습니다.");
        }
    }
%>
