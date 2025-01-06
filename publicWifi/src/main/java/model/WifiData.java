package model;

public class WifiData {

	private String mgrNo; // 관리번호
	private String wrdofc; // 자치구
	private String mainNm; // 와이파이명
	private String adres1; // 도로명주소
	private String adres2; // 상세주소
	private String instlFloor; // 설치위치(층)
	private String instlTy; // 설치유형
	private String instlMby; // 설치기관
	private String svcSe; // 서비스구분
	private String cmcwr; // 망종류
	private Integer cnstcYear; // 설치년도
	private String inoutDoor; // 실내외구분
	private String remars3; // wifi접속환경
	private double lnt; // X좌표
	private double lat; // Y좌표
	private String workDttm; // 작업일자
	private int wifiCount;
	Float distance;

	// 기본 생성자
	public WifiData() {
	}

	// 모든 필드를 포함한 생성자
	public WifiData(String mgrNo, String wrdofc, String mainNm, String adres1, String adres2, String instlFloor,
			String instlTy, String instlMby, String svcSe, String cmcwr, Integer cnstcYear, String inoutDoor,
			String remars3, double lnt, double lat, String workDttm) {
		this.mgrNo = mgrNo;
		this.wrdofc = wrdofc;
		this.mainNm = mainNm;
		this.adres1 = adres1;
		this.adres2 = adres2;
		this.instlFloor = instlFloor;
		this.instlTy = instlTy;
		this.instlMby = instlMby;
		this.svcSe = svcSe;
		this.cmcwr = cmcwr;
		this.cnstcYear = cnstcYear;
		this.inoutDoor = inoutDoor;
		this.remars3 = remars3;
		this.lnt = lnt;
		this.lat = lat;
		this.workDttm = workDttm;
	}

	public String getMgrNo() {
		return mgrNo;
	}

	public void setMgrNo(String mgrNo) {
		this.mgrNo = mgrNo;
	}

	public String getWrdofc() {
		return wrdofc;
	}

	public void setWrdofc(String wrdofc) {
		this.wrdofc = wrdofc;
	}

	public String getMainNm() {
		return mainNm;
	}

	public void setMainNm(String mainNm) {
		this.mainNm = mainNm;
	}

	public String getAdres1() {
		return adres1;
	}

	public void setAdres1(String adres1) {
		this.adres1 = adres1;
	}

	public String getAdres2() {
		return adres2;
	}

	public void setAdres2(String adres2) {
		this.adres2 = adres2;
	}

	public String getInstlFloor() {
		return instlFloor;
	}

	public void setInstlFloor(String instlFloor) {
		this.instlFloor = instlFloor;
	}

	public String getInstlTy() {
		return instlTy;
	}

	public void setInstlTy(String instlTy) {
		this.instlTy = instlTy;
	}

	public String getInstlMby() {
		return instlMby;
	}

	public void setInstlMby(String instlMby) {
		this.instlMby = instlMby;
	}

	public String getSvcSe() {
		return svcSe;
	}

	public void setSvcSe(String svcSe) {
		this.svcSe = svcSe;
	}

	public String getCmcwr() {
		return cmcwr;
	}

	public void setCmcwr(String cmcwr) {
		this.cmcwr = cmcwr;
	}

	public Integer getCnstcYear() {
		return cnstcYear;
	}

	public void setCnstcYear(Integer cnstcYear) {
		this.cnstcYear = cnstcYear;
	}

	public String getInoutDoor() {
		return inoutDoor;
	}

	public void setInoutDoor(String inoutDoor) {
		this.inoutDoor = inoutDoor;
	}

	public String getRemars3() {
		return remars3;
	}

	public void setRemars3(String remars3) {
		this.remars3 = remars3;
	}

	public double getLnt() {
		return lnt;
	}

	public void setLnt(double lnt) {
		this.lnt = lnt;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getWorkDttm() {
		return workDttm;
	}

	public void setWorkDttm(String workDttm) {
		this.workDttm = workDttm;
	}

	public int getWifiCount() {
		return wifiCount;
	}

	public void setWifiCount(int wifiCount) {
		this.wifiCount = wifiCount;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

}


//public WifiData() {
//// 1. 기존 데이터 삭제
////WifiService.deleteAllWifiData(); // 모든 기존 와이파이 데이터를 삭제
//
//// 2. ApiExplorer 실행 (main 메서드 호출)
//try {
//    ApiExplorer.main(new String[0]);
//} catch (IOException | InterruptedException e) {
//    e.printStackTrace();
//}
//// 3. Wifi 데이터 개수 가져오기
//this.wifiCount = WifiService.getWifiDataCount();
//}
