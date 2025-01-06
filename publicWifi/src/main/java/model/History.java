package model;

import java.sql.Timestamp;

public class History {

	private int id;
	private double lnt; // X좌표
	private double lat; // Y좌표
	private Timestamp createdTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

}
