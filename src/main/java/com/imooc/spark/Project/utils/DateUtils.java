package com.imooc.spark.Project.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;


public class DateUtils {
	public static FastDateFormat outDF = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	public static FastDateFormat inDF = FastDateFormat.getInstance("yyyyMMddHHmmss");

	public static Long getTime(String time) throws ParseException {
		return outDF.parse(time).getTime();
	}

	public static String parseToMinute(String time) throws ParseException {
		return inDF.format(new Date(getTime(time)));
	}

}
