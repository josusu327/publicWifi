package Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.History;
import model.WifiData;

public class WifiService {

	// MariaDB 연결 정보
	private static final String URL = "jdbc:mariadb://localhost:3306/publicWifi_db"; // 데이터베이스 이름과 포트를 맞게 수정
	private static final String USER = "publicWifi_user"; // MariaDB 계정 이름
	private static final String PASSWORD = "zerobase"; // MariaDB 비밀번호

	// MariaDB 연결 메서드
	public static Connection getConnection() throws SQLException {
		try {
			// MariaDB 드라이버 로드
			Class.forName("org.mariadb.jdbc.Driver");
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("MariaDB JDBC 드라이버를 로드할 수 없습니다.");
		}
	}

	// 공공와이파이 데이터 저장 메서드
//	public static void saveWifiData(String mgrNo, String wrdofc, String mainNm, String adres1, String adres2,
//	        String instlFloor, String instlTy, String instlMby, String svcSe, String cmcwr, Integer cnstcYear,
//	        String inoutDoor, String remars3, double lnt, double lat, String workDttm) {
//	    String sql = "INSERT INTO WIFI (X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, "
//	            + "X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, "
//	            + "X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, "
//	            + "X_SWIFI_REMARS3, LNT, LAT, WORK_DTTM) "
//	            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
//	            + "ON DUPLICATE KEY UPDATE "
//	            + "X_SWIFI_WRDOFC = VALUES(X_SWIFI_WRDOFC), "
//	            + "X_SWIFI_MAIN_NM = VALUES(X_SWIFI_MAIN_NM), "
//	            + "X_SWIFI_ADRES1 = VALUES(X_SWIFI_ADRES1), "
//	            + "X_SWIFI_ADRES2 = VALUES(X_SWIFI_ADRES2), "
//	            + "X_SWIFI_INSTL_FLOOR = VALUES(X_SWIFI_INSTL_FLOOR), "
//	            + "X_SWIFI_INSTL_TY = VALUES(X_SWIFI_INSTL_TY), "
//	            + "X_SWIFI_INSTL_MBY = VALUES(X_SWIFI_INSTL_MBY), "
//	            + "X_SWIFI_SVC_SE = VALUES(X_SWIFI_SVC_SE), "
//	            + "X_SWIFI_CMCWR = VALUES(X_SWIFI_CMCWR), "
//	            + "X_SWIFI_CNSTC_YEAR = VALUES(X_SWIFI_CNSTC_YEAR), "
//	            + "X_SWIFI_INOUT_DOOR = VALUES(X_SWIFI_INOUT_DOOR), "
//	            + "X_SWIFI_REMARS3 = VALUES(X_SWIFI_REMARS3), "
//	            + "LNT = VALUES(LNT), "
//	            + "LAT = VALUES(LAT), "
//	            + "WORK_DTTM = VALUES(WORK_DTTM)";
//
//	    try (Connection connection = getConnection();
//	            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//	        // 파라미터 설정
//	        preparedStatement.setString(1, mgrNo);
//	        preparedStatement.setString(2, wrdofc);
//	        preparedStatement.setString(3, mainNm);
//	        preparedStatement.setString(4, adres1);
//	        preparedStatement.setString(5, adres2);
//	        preparedStatement.setString(6, instlFloor);
//	        preparedStatement.setString(7, instlTy);
//	        preparedStatement.setString(8, instlMby);
//	        preparedStatement.setString(9, svcSe);
//	        preparedStatement.setString(10, cmcwr);
//	        preparedStatement.setObject(11, cnstcYear); // null 가능
//	        preparedStatement.setString(12, inoutDoor);
//	        preparedStatement.setString(13, remars3);
//	        preparedStatement.setDouble(14, lnt);
//	        preparedStatement.setDouble(15, lat);
//	        preparedStatement.setString(16, workDttm);
//
//	        // SQL 실행
//	        int rowsInserted = preparedStatement.executeUpdate();
//
//	    } catch (SQLException e) {
//	        System.err.println("데이터 삽입 중 오류 발생: " + e.getMessage());
//	    }
//	}
	
	public static void saveWifiDataBatch(List<WifiData> wifiDataList) {
	    String sql = "INSERT INTO WIFI (X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, "
	               + "X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, "
	               + "X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, "
	               + "X_SWIFI_REMARS3, LNT, LAT, WORK_DTTM) "
	               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
	               + "ON DUPLICATE KEY UPDATE "
	               + "X_SWIFI_WRDOFC = VALUES(X_SWIFI_WRDOFC), "
	               + "X_SWIFI_MAIN_NM = VALUES(X_SWIFI_MAIN_NM), "
	               + "X_SWIFI_ADRES1 = VALUES(X_SWIFI_ADRES1), "
	               + "X_SWIFI_ADRES2 = VALUES(X_SWIFI_ADRES2), "
	               + "X_SWIFI_INSTL_FLOOR = VALUES(X_SWIFI_INSTL_FLOOR), "
	               + "X_SWIFI_INSTL_TY = VALUES(X_SWIFI_INSTL_TY), "
	               + "X_SWIFI_INSTL_MBY = VALUES(X_SWIFI_INSTL_MBY), "
	               + "X_SWIFI_SVC_SE = VALUES(X_SWIFI_SVC_SE), "
	               + "X_SWIFI_CMCWR = VALUES(X_SWIFI_CMCWR), "
	               + "X_SWIFI_CNSTC_YEAR = VALUES(X_SWIFI_CNSTC_YEAR), "
	               + "X_SWIFI_INOUT_DOOR = VALUES(X_SWIFI_INOUT_DOOR), "
	               + "X_SWIFI_REMARS3 = VALUES(X_SWIFI_REMARS3), "
	               + "LNT = VALUES(LNT), "
	               + "LAT = VALUES(LAT), "
	               + "WORK_DTTM = VALUES(WORK_DTTM)";

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        for (WifiData wifiData : wifiDataList) {
	            preparedStatement.setString(1, wifiData.getMgrNo());
	            preparedStatement.setString(2, wifiData.getWrdofc());
	            preparedStatement.setString(3, wifiData.getMainNm());
	            preparedStatement.setString(4, wifiData.getAdres1());
	            preparedStatement.setString(5, wifiData.getAdres2());
	            preparedStatement.setString(6, wifiData.getInstlFloor());
	            preparedStatement.setString(7, wifiData.getInstlTy());
	            preparedStatement.setString(8, wifiData.getInstlMby());
	            preparedStatement.setString(9, wifiData.getSvcSe());
	            preparedStatement.setString(10, wifiData.getCmcwr());
	            preparedStatement.setObject(11, wifiData.getCnstcYear());
	            preparedStatement.setString(12, wifiData.getInoutDoor());
	            preparedStatement.setString(13, wifiData.getRemars3());
	            preparedStatement.setDouble(14, wifiData.getLnt());
	            preparedStatement.setDouble(15, wifiData.getLat());
	            preparedStatement.setString(16, wifiData.getWorkDttm());

	            preparedStatement.addBatch();
	        }

	        preparedStatement.executeBatch();

	    } catch (SQLException e) {
	        System.err.println("데이터 배치 삽입 중 오류 발생: " + e.getMessage());
	    }
	}



	// 와이파이 데이터 개수를 반환하는 메서드
    public static int getWifiDataCount() {
        String sql = "SELECT count(*) FROM WIFI";
        int count = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt(1); // 첫 번째 컬럼(개수)을 가져옴
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }


	// 공공와이파이 데이터 삭제 메서드 (전체 데이터 삭제)
	public static void deleteAllWifiData() {
	    String sql = "DELETE FROM WIFI"; // 조건 없이 모든 데이터 삭제

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        // SQL 실행
	        int rowsDeleted = preparedStatement.executeUpdate();
	        if (rowsDeleted > 0) {
	            System.out.println("모든 공공와이파이 데이터가 성공적으로 삭제되었습니다!");
	        } else {
	            System.out.println("삭제할 데이터가 없습니다.");
	        }

	    } catch (SQLException e) {
	        System.err.println("데이터 삭제 중 오류 발생: " + e.getMessage());
	    }
	}


	// 위도(LAT)와 경도(LNT)를 기준으로 근처 WiFi 정보 검색
	public static List<WifiData> getNearbyWifi(double lnt,double lat) {
	    List<WifiData> wifiList = new ArrayList<>();
	    String sql = """
	        SELECT *,
	            (6371 * acos(
	                cos(radians(?)) * cos(radians(LAT)) * cos(radians(LNT) - radians(?)) +
	                sin(radians(?)) * sin(radians(LAT))
	            )) AS distance
	        FROM WIFI
	        ORDER BY distance
	        LIMIT 20;
	    """;

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        // SQL 쿼리에 매개변수 바인딩
	        preparedStatement.setDouble(1, lat);
	        preparedStatement.setDouble(2, lnt);
	        preparedStatement.setDouble(3, lat);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                WifiData wifi = new WifiData();
	                wifi.setMgrNo(resultSet.getString("X_SWIFI_MGR_NO"));
	                wifi.setWrdofc(resultSet.getString("X_SWIFI_WRDOFC"));
	                wifi.setMainNm(resultSet.getString("X_SWIFI_MAIN_NM"));
	                wifi.setAdres1(resultSet.getString("X_SWIFI_ADRES1"));
	                wifi.setAdres2(resultSet.getString("X_SWIFI_ADRES2"));
	                wifi.setInstlFloor(resultSet.getString("X_SWIFI_INSTL_FLOOR"));
	                wifi.setInstlTy(resultSet.getString("X_SWIFI_INSTL_TY"));
	                wifi.setInstlMby(resultSet.getString("X_SWIFI_INSTL_MBY"));
	                wifi.setSvcSe(resultSet.getString("X_SWIFI_SVC_SE"));
	                wifi.setCmcwr(resultSet.getString("X_SWIFI_CMCWR"));
	                wifi.setCnstcYear(resultSet.getInt("X_SWIFI_CNSTC_YEAR"));
	                wifi.setInoutDoor(resultSet.getString("X_SWIFI_INOUT_DOOR"));
	                wifi.setRemars3(resultSet.getString("X_SWIFI_REMARS3"));
	                wifi.setLnt(resultSet.getDouble("LNT"));
	                wifi.setLat(resultSet.getDouble("LAT"));
	                wifi.setWorkDttm(resultSet.getString("WORK_DTTM"));

	                // 거리 계산된 값 설정
	                float distance = resultSet.getFloat("distance");
	                wifi.setDistance(distance);

	                wifiList.add(wifi);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return wifiList;
	}

	// 히스토리 생성
	public static void saveHistory(double lat, double lnt) {
        String sql = "INSERT INTO HISTORY (LAT, LNT, CREATED_TIME) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, lat);
            preparedStatement.setDouble(2, lnt);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis())); 


            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	// 히스토리 목록 보기
    public static List<History> getHistoryList() {
        List<History> historyList = new ArrayList<>();
        String sql = "SELECT * FROM HISTORY ORDER BY ID DESC";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                History history = new History();
                history.setId(resultSet.getInt("ID"));
                history.setLat(resultSet.getDouble("LAT"));
                history.setLnt(resultSet.getDouble("LNT"));
                history.setCreatedTime(resultSet.getTimestamp("CREATED_TIME"));

                historyList.add(history);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }

    // 히스토리 삭제
    public static void deleteHistory(int id) {
        String sql = "DELETE FROM HISTORY WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

//공공와이파이 데이터를 조회하여 List로 반환
//	public static List<String[]> getAllWifiDataAsList() {
//		String sql = "SELECT X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, "
//				+ "X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, "
//				+ "X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, "
//				+ "X_SWIFI_REMARS3, LNT, LAT, WORK_DTTM FROM WIFI";
//		List<String[]> wifiDataList = new ArrayList<>();
//
//		try (Connection connection = getConnection();
//				PreparedStatement preparedStatement = connection.prepareStatement(sql);
//				ResultSet resultSet = preparedStatement.executeQuery()) {
//
//			while (resultSet.next()) {
//				String[] row = new String[16];
//				row[0] = resultSet.getString("X_SWIFI_MGR_NO");
//				row[1] = resultSet.getString("X_SWIFI_WRDOFC");
//				row[2] = resultSet.getString("X_SWIFI_MAIN_NM");
//				row[3] = resultSet.getString("X_SWIFI_ADRES1");
//				row[4] = resultSet.getString("X_SWIFI_ADRES2");
//				row[5] = resultSet.getString("X_SWIFI_INSTL_FLOOR");
//				row[6] = resultSet.getString("X_SWIFI_INSTL_TY");
//				row[7] = resultSet.getString("X_SWIFI_INSTL_MBY");
//				row[8] = resultSet.getString("X_SWIFI_SVC_SE");
//				row[9] = resultSet.getString("X_SWIFI_CMCWR");
//				row[10] = resultSet.getString("X_SWIFI_CNSTC_YEAR");
//				row[11] = resultSet.getString("X_SWIFI_INOUT_DOOR");
//				row[12] = resultSet.getString("X_SWIFI_REMARS3");
//				row[13] = String.valueOf(resultSet.getDouble("LNT"));
//				row[14] = String.valueOf(resultSet.getDouble("LAT"));
//				row[15] = resultSet.getString("WORK_DTTM");
//
//				wifiDataList.add(row);
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return wifiDataList;
//	}
