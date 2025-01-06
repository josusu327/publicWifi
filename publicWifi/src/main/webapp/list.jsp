<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="Service.WifiService"%>
<%@ page import="model.WifiData"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>와이파이 정보 구하기</title>
</head>
<body>
    <div class="container">
        <h1>와이파이 정보 구하기</h1>
        <div class="nav-links">
            <a href="index.jsp">홈</a>
            <a href="history.jsp">위치 히스토리 목록</a>
            <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
        </div>
        <form action="list.jsp" method="get">
            LNT : <input type="text" placeholder="LNT: 0.0" name="lnt" id="lnt" value="<%= request.getParameter("lnt") != null ? request.getParameter("lnt") : "" %>" readonly>
            LAT : <input type="text" placeholder="LAT: 0.0" name="lat" id="lat" value="<%= request.getParameter("lat") != null ? request.getParameter("lat") : "" %>" readonly> 
            <button type="button" onclick="getLocation();">내 위치 가져오기</button>
            <button type="submit">근처 WIFI 정보 보기</button>
        </form>
        
        
        <!-- 위치 히스토리 저장한게 history db로 저장되지 않음. -->

        <%
            // LAT와 LNT 값을 가져옴
            String latParam = request.getParameter("lat");
            String lntParam = request.getParameter("lnt");

            if (latParam != null && lntParam != null) {
                double lat = Double.parseDouble(latParam);
                double lnt = Double.parseDouble(lntParam);

                // 위치 히스토리 저장
                WifiService.saveHistory(lat, lnt); 

                // WifiService를 이용해 근처 와이파이 정보 가져오기
                List<WifiData> wifiList = WifiService.getNearbyWifi(lnt, lat);

                if (wifiList.isEmpty()) {
        %>
        <p>근처에 와이파이가 없습니다.</p>
        <%
                } else {
        %>

        <table>
            <thead>
                <tr>
                    <th>거리(Km)</th>
                    <th>관리번호</th>
                    <th>자치구</th>
                    <th>와이파이명</th>
                    <th>도로명주소</th>
                    <th>상세주소</th>
                    <th>설치위치(층)</th>
                    <th>설치유형</th>
                    <th>설치기관</th>
                    <th>서비스구분</th>
                    <th>망종류</th>
                    <th>설치년도</th>
                    <th>실내외구분</th>
                    <th>WIFI접속환경</th>
                    <th>X좌표</th>
                    <th>Y좌표</th>
                    <th>작업일자</th>
                </tr>
            </thead>
            <tbody>
                <%
                    // 근처 와이파이 정보 출력
                    for (WifiData wifi : wifiList) {
                %>
                <tr>
                    <td><%= wifi.getDistance() %></td> <!-- 거리 Km (추가 로직 필요) -->
                    <td><%= wifi.getMgrNo() %></td>
                    <td><%= wifi.getWrdofc() %></td>
                    <td><%= wifi.getMainNm() %></td>
                    <td><%= wifi.getAdres1() %></td>
                    <td><%= wifi.getAdres2() %></td>
                    <td><%= wifi.getInstlFloor() %></td>
                    <td><%= wifi.getInstlTy() %></td>
                    <td><%= wifi.getInstlMby() %></td>
                    <td><%= wifi.getSvcSe() %></td>
                    <td><%= wifi.getCmcwr() %></td>
                    <td><%= wifi.getCnstcYear() %></td>
                    <td><%= wifi.getInoutDoor() %></td>
                    <td><%= wifi.getRemars3() %></td>
                    <td><%= wifi.getLnt() %></td>
                    <td><%= wifi.getLat() %></td>
                    <td><%= wifi.getWorkDttm() %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <%
                }
            }
        %>
    </div>
</body>

<script>
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
