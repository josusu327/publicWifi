<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.WifiData"%>
<%@ page import="Service.WifiService"%>
<%@ page import="test.ApiExplorer" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
<style>
    /* 텍스트를 가로로 중앙 정렬 */
    body {
        text-align: center;
        font-family: Arial, sans-serif;
    }
</style>

</head>
<body>
    <%
        try {
            // Open API 데이터 가져오기 및 DB 저장
            ApiExplorer.fetchAndSaveWifiData();

            // 저장된 Wi-Fi 데이터 개수 가져오기
            int wifiCount = WifiService.getWifiDataCount();

            // 성공 메시지 출력
            out.println("<h1>" + wifiCount + "개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>");
        } catch (Exception e) {
            // 에러 메시지 출력
            out.println("<h1>와이파이 정보 저장 중 오류가 발생하였습니다.</h1>");
            out.println("<p>오류 메시지: " + e.getMessage() + "</p>");
        }
    %>
    <a href="index.jsp">홈으로 돌아가기</a>
</body>
</html>
