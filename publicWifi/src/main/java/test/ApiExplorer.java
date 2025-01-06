package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import Service.WifiService;

public class ApiExplorer {

    // HttpClient를 전역으로 선언하여 재사용
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
        // API 기본 URL 생성
        String baseUrl = "http://openapi.seoul.go.kr:8088";
        String apiKey = "546c6869517768743638665976714f"; // 인증키
        String serviceName = "TbPublicWifiInfo";

        // Step 1: 총 데이터 개수 확인
        String initialUrl = buildUrl(baseUrl, apiKey, serviceName, 1, 1);
        HttpResponse<String> initialResponse = sendRequest(initialUrl);

        if (initialResponse.statusCode() >= 200 && initialResponse.statusCode() <= 300) {
            String initialResponseBody = initialResponse.body();
            int totalCount = extractTotalCount(initialResponseBody);
            System.out.println("Total Wi-Fi Count: " + totalCount);

            // Step 2: 데이터 가져오기 및 DB 저장
            fetchAndSaveWifiData(baseUrl, apiKey, serviceName, totalCount);

        } else {
            System.err.println("Failed to fetch total count: " + initialResponse.body());
        }
    }

    // 총 데이터 개수를 추출하는 메서드
    private static int extractTotalCount(String responseBody) {
        String totalCountKey = "\"list_total_count\":";
        int startIndex = responseBody.indexOf(totalCountKey);
        if (startIndex == -1) {
			return 0;
		}
        startIndex += totalCountKey.length();
        int endIndex = responseBody.indexOf(",", startIndex);
        return Integer.parseInt(responseBody.substring(startIndex, endIndex).trim());
    }

    // Wi-Fi 데이터를 가져오고 DB에 저장하는 메서드
    private static void fetchAndSaveWifiData(String baseUrl, String apiKey, String serviceName, int totalCount)
            throws IOException, InterruptedException {
        int batchSize = 1000;

        for (int i = 1; i <= totalCount; i += batchSize) {
            int endIndex = Math.min(i + batchSize - 1, totalCount);
            String url = buildUrl(baseUrl, apiKey, serviceName, i, endIndex);

            try {
                HttpResponse<String> response = sendRequest(url);

                if (response.statusCode() >= 200 && response.statusCode() <= 300) {
                    parseAndSaveWifiData(response.body());
                } else {
                    System.err.println("Failed to fetch data for batch [" + i + " - " + endIndex + "]");
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Error fetching data for batch [" + i + " - " + endIndex + "]: " + e.getMessage());
                Thread.sleep(5000); // 에러 발생 시 5초 대기
            }

            // API 호출 간 100ms 대기
            Thread.sleep(100);
        }
    }

    // JSON 데이터를 파싱하고 DB에 저장하는 메서드
    private static void parseAndSaveWifiData(String responseBody) {
        String[] wifiEntries = responseBody.split("},\\{");
        for (String entry : wifiEntries) {
            try {
                String mgrNo = extractJsonField(entry, "X_SWIFI_MGR_NO");
                String wrdofc = extractJsonField(entry, "X_SWIFI_WRDOFC");
                String mainNm = extractJsonField(entry, "X_SWIFI_MAIN_NM");
                String adres1 = extractJsonField(entry, "X_SWIFI_ADRES1");
                String adres2 = extractJsonField(entry, "X_SWIFI_ADRES2");
                String instlFloor = extractJsonField(entry, "X_SWIFI_INSTL_FLOOR");
                String instlTy = extractJsonField(entry, "X_SWIFI_INSTL_TY");
                String instlMby = extractJsonField(entry, "X_SWIFI_INSTL_MBY");
                String svcSe = extractJsonField(entry, "X_SWIFI_SVC_SE");
                String cmcwr = extractJsonField(entry, "X_SWIFI_CMCWR");
                Integer cnstcYear = extractJsonField(entry, "X_SWIFI_CNSTC_YEAR") != null
                        ? Integer.parseInt(extractJsonField(entry, "X_SWIFI_CNSTC_YEAR"))
                        : null;
                String inoutDoor = extractJsonField(entry, "X_SWIFI_INOUT_DOOR");
                String remars3 = extractJsonField(entry, "X_SWIFI_REMARS3");
                double lnt = extractJsonField(entry, "LNT") != null
                        ? Double.parseDouble(extractJsonField(entry, "LNT"))
                        : 0.0;
                double lat = extractJsonField(entry, "LAT") != null
                        ? Double.parseDouble(extractJsonField(entry, "LAT"))
                        : 0.0;
                String workDttm = extractJsonField(entry, "WORK_DTTM");

                WifiService.saveWifiData(mgrNo, wrdofc, mainNm, adres1, adres2, instlFloor, instlTy, instlMby, svcSe,
                        cmcwr, cnstcYear, inoutDoor, remars3, lnt, lat, workDttm);
            } catch (Exception e) {
                System.err.println("Error saving Wi-Fi data: " + e.getMessage());
            }
        }
    }

    // JSON에서 특정 필드 값을 추출하는 메서드
    private static String extractJsonField(String json, String fieldName) {
        String fieldKey = "\"" + fieldName + "\":\"";
        int startIndex = json.indexOf(fieldKey);
        if (startIndex == -1) {
			return null;
		}
        startIndex += fieldKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }

    // URL을 빌드하는 메서드
    private static String buildUrl(String baseUrl, String apiKey, String serviceName, int start, int end)
            throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("/" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8)); // 인증키
        urlBuilder.append("/" + URLEncoder.encode("json", StandardCharsets.UTF_8)); // 요청파일타입
        urlBuilder.append("/" + URLEncoder.encode(serviceName, StandardCharsets.UTF_8)); // 서비스명
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(start), StandardCharsets.UTF_8)); // 시작위치
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(end), StandardCharsets.UTF_8)); // 종료위치
        return urlBuilder.toString();
    }

    // HTTP 요청을 보내는 메서드
    private static HttpResponse<String> sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();
        return client.send(request, BodyHandlers.ofString());
    }
}
