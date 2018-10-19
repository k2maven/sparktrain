package com.imooc.spark.Project.dao;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

import com.imooc.spark.Project.domin.CourseSearchClick;
import com.imooc.spark.Project.utils.HBaseUtils;

public class CourseSearchClickCountDAO {
	private static final String tableName = "imooc_course_search_clickcount";
	private static final String cf = "info";
	private static final String qualifer = "click_count";
	/**
	 * 保存数据到HBase
	 * 
	 * @param list
	 * @throws IOException
	 */

	public static void save(List<CourseSearchClick> list) throws IOException {
		HTable table = HBaseUtils.getInstance().getTable(tableName);
		for (CourseSearchClick courseSearchClick : list) {
			table.incrementColumnValue(Bytes.toBytes(courseSearchClick.getDay_search_course()), Bytes.toBytes(cf),
					Bytes.toBytes(qualifer), courseSearchClick.getClick_count());
		}
	}
	
	/**
	 * 根据rowkey查询值
	 * 
	 * @param day_course
	 * @return
	 * @throws IOException
	 */
	public static Long count(String day_search_course) throws IOException {
		HTable table = HBaseUtils.getInstance().getTable(tableName);
		Get get = new Get(Bytes.toBytes(day_search_course));
		byte[] value = table.get(get).getValue(cf.getBytes(), qualifer.getBytes());
		if (value == null) {
			return 0L;
		}
		return Bytes.toLong(value);
	}
}
