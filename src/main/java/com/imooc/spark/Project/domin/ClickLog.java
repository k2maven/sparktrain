package com.imooc.spark.Project.domin;

import java.io.Serializable;

public class ClickLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ip;
	private String time;
	private Integer courseId;
	private Integer statusCode;
	private String referer;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public ClickLog(String ip, String time, Integer courseId, Integer statusCode, String referer) {
		super();
		this.ip = ip;
		this.time = time;
		this.courseId = courseId;
		this.statusCode = statusCode;
		this.referer = referer;
	}

	public ClickLog() {
		super();
	}

}
