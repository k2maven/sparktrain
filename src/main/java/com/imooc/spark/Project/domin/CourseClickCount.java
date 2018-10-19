package com.imooc.spark.Project.domin;

import java.io.Serializable;

public class CourseClickCount implements Serializable {

	private static final long serialVersionUID = 1L;
	private String day_course;
	private Long click_count;

	public String getDay_course() {
		return day_course;
	}

	public void setDay_course(String day_course) {
		this.day_course = day_course;
	}

	public Long getClick_count() {
		return click_count;
	}

	public void setClick_count(Long click_count) {
		this.click_count = click_count;
	}

	public CourseClickCount(String day_course, Long click_count) {
		super();
		this.day_course = day_course;
		this.click_count = click_count;
	}

	public CourseClickCount() {
		super();
	}

}
