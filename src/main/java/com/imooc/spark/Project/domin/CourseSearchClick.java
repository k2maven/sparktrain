package com.imooc.spark.Project.domin;

import java.io.Serializable;

public class CourseSearchClick implements Serializable {
	private static final long serialVersionUID = 1L;
	private String day_search_course;
	private Long click_count;

	public String getDay_search_course() {
		return day_search_course;
	}

	public void setDay_search_course(String day_search_course) {
		this.day_search_course = day_search_course;
	}

	public Long getClick_count() {
		return click_count;
	}

	public void setClick_count(Long click_count) {
		this.click_count = click_count;
	}

	public CourseSearchClick(String day_search_course, Long click_count) {
		super();
		this.day_search_course = day_search_course;
		this.click_count = click_count;
	}

	public CourseSearchClick() {
		super();
	}

}
