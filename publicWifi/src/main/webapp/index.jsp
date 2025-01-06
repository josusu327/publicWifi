<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>와이파이 정보 구하기</title>
</head>
<body>
    <div class="container">
    <h1>와이파이 정보 구하기</h1>
    <div class="nav-links">
        <a href="index.jsp">홈</a> 
        <a href="history.jsp">위치 히스토리 목록</a> 
        <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>  <!-- 누르면 오픈api호출 -->
    </div>
    <form action="list.jsp" method="get">
        LNT : <input type="text" placeholder="LNT: 0.0" name="lnt" id="lnt" readonly>
        LAT : <input type="text" placeholder="LAT: 0.0" name="lat" id="lat" readonly> 
        <button type="button" onclick="getLocation();">내 위치 가져오기</button>
        <button type="submit">근처 WIFI 정보 보기</button>  <!-- 이 버튼을 클릭하면 list.jsp로 이동 -->
    </form>
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
                <tr>
                    <td colspan="17" class="empty-message">위치 정보를 입력한 후 조회해 주세요.</td>
                </tr>
            </tbody>
        </table>
</div>

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

</body>
</html>
