package test;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import Service.WifiService;
import model.WifiData;

public class ApiExplorer {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException, InterruptedException {
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

            // Step 2: 데이터 가져와서 저장
            for (int start = 1; start <= totalCount; start += 1000) {
                int end = Math.min(start + 999, totalCount);
                String dataUrl = buildUrl(baseUrl, apiKey, serviceName, start, end);
                HttpResponse<String> dataResponse = sendRequest(dataUrl);

                if (dataResponse.statusCode() >= 200 && dataResponse.statusCode() <= 300) {
                    parseAndSaveWifiData(dataResponse.body());
                } else {
                    System.err.println("Failed to fetch data for range [" + start + " - " + end + "]");
                }

                Thread.sleep(100); // API 호출 간 대기
            }
        } else {
            System.err.println("Failed to fetch total count: " + initialResponse.body());
        }
    }

    private static int extractTotalCount(String responseBody) {
        String totalCountKey = "\"list_total_count\":";
        int startIndex = responseBody.indexOf(totalCountKey);
        if (startIndex == -1) {
            System.err.println("Failed to extract total count from response.");
            return 0;
        }
        startIndex += totalCountKey.length();
        int endIndex = responseBody.indexOf(",", startIndex);
        return Integer.parseInt(responseBody.substring(startIndex, endIndex).trim());
    }

//    private static void parseAndSaveWifiData(String responseBody) {
//        String[] wifiEntries = responseBody.split("},\\{");
//        for (String entry : wifiEntries) {
//            try {
//                String mgrNo = extractJsonField(entry, "X_SWIFI_MGR_NO");
//                String wrdofc = extractJsonField(entry, "X_SWIFI_WRDOFC");
//                String mainNm = extractJsonField(entry, "X_SWIFI_MAIN_NM");
//                String adres1 = extractJsonField(entry, "X_SWIFI_ADRES1");
//                String adres2 = extractJsonField(entry, "X_SWIFI_ADRES2");
//                String instlFloor = extractJsonField(entry, "X_SWIFI_INSTL_FLOOR");
//                String instlTy = extractJsonField(entry, "X_SWIFI_INSTL_TY");
//                String instlMby = extractJsonField(entry, "X_SWIFI_INSTL_MBY");
//                String svcSe = extractJsonField(entry, "X_SWIFI_SVC_SE");
//                String cmcwr = extractJsonField(entry, "X_SWIFI_CMCWR");
//                Integer cnstcYear = extractJsonField(entry, "X_SWIFI_CNSTC_YEAR") != null
//                        ? Integer.parseInt(extractJsonField(entry, "X_SWIFI_CNSTC_YEAR"))
//                        : null;
//                String inoutDoor = extractJsonField(entry, "X_SWIFI_INOUT_DOOR");
//                String remars3 = extractJsonField(entry, "X_SWIFI_REMARS3");
//                double lnt = extractJsonField(entry, "LNT") != null
//                        ? Double.parseDouble(extractJsonField(entry, "LNT"))
//                        : 0.0;
//                double lat = extractJsonField(entry, "LAT") != null
//                        ? Double.parseDouble(extractJsonField(entry, "LAT"))
//                        : 0.0;
//                String workDttm = extractJsonField(entry, "WORK_DTTM");
//
//                WifiService.saveWifiData(mgrNo, wrdofc, mainNm, adres1, adres2, instlFloor, instlTy, instlMby, svcSe,
//                        cmcwr, cnstcYear, inoutDoor, remars3, lnt, lat, workDttm);
//            } catch (Exception e) {
//                System.err.println("Error saving Wi-Fi data: " + e.getMessage());
//            }
//        }
//    }
    private static void parseAndSaveWifiData(String responseBody) {
        // Wi-Fi 데이터를 저장할 리스트 생성
        List<WifiData> wifiDataList = new ArrayList<>();
        String[] wifiEntries = responseBody.split("},\\{");

        for (String entry : wifiEntries) {
            try {
                WifiData wifiData = new WifiData(
                    extractJsonField(entry, "X_SWIFI_MGR_NO"),
                    extractJsonField(entry, "X_SWIFI_WRDOFC"),
                    extractJsonField(entry, "X_SWIFI_MAIN_NM"),
                    extractJsonField(entry, "X_SWIFI_ADRES1"),
                    extractJsonField(entry, "X_SWIFI_ADRES2"),
                    extractJsonField(entry, "X_SWIFI_INSTL_FLOOR"),
                    extractJsonField(entry, "X_SWIFI_INSTL_TY"),
                    extractJsonField(entry, "X_SWIFI_INSTL_MBY"),
                    extractJsonField(entry, "X_SWIFI_SVC_SE"),
                    extractJsonField(entry, "X_SWIFI_CMCWR"),
                    extractJsonField(entry, "X_SWIFI_CNSTC_YEAR") != null ? Integer.parseInt(extractJsonField(entry, "X_SWIFI_CNSTC_YEAR")) : null,
                    extractJsonField(entry, "X_SWIFI_INOUT_DOOR"),
                    extractJsonField(entry, "X_SWIFI_REMARS3"),
                    extractJsonField(entry, "LNT") != null ? Double.parseDouble(extractJsonField(entry, "LNT")) : 0.0,
                    extractJsonField(entry, "LAT") != null ? Double.parseDouble(extractJsonField(entry, "LAT")) : 0.0,
                    extractJsonField(entry, "WORK_DTTM")
                );

                wifiDataList.add(wifiData);

            } catch (Exception e) {
                System.err.println("Error parsing Wi-Fi data: " + e.getMessage());
            }
        }

        // 배치 저장 호출
        WifiService.saveWifiDataBatch(wifiDataList);
    }

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

    private static String buildUrl(String baseUrl, String apiKey, String serviceName, int start, int end) {
        return baseUrl + "/" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8) +
                "/" + URLEncoder.encode("json", StandardCharsets.UTF_8) +
                "/" + URLEncoder.encode(serviceName, StandardCharsets.UTF_8) +
                "/" + URLEncoder.encode(String.valueOf(start), StandardCharsets.UTF_8) +
                "/" + URLEncoder.encode(String.valueOf(end), StandardCharsets.UTF_8);
    }

    private static HttpResponse<String> sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .build();
        return client.send(request, BodyHandlers.ofString());
    }
}
