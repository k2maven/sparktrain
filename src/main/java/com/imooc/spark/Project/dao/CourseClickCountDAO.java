package com.imooc.spark.Project.dao;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

import com.imooc.spark.Project.domin.CourseClickCount;
import com.imooc.spark.Project.utils.HBaseUtils;

public class CourseClickCountDAO {
	private static final String tableName = "imooc_course_clickcount";
	private static final String cf = "info";
	private static final String qualifer = "click_count";

	/**
	 * 保存数据到HBase
	 * 
	 * @param list
	 * @throws IOException
	 */

	public static void save(List<CourseClickCount> list) throws IOException {
		HTable table = HBaseUtils.getInstance().getTable(tableName);
		for (CourseClickCount courseClickCount : list) {
			table.incrementColumnValue(Bytes.toBytes(courseClickCount.getDay_course()), Bytes.toBytes(cf),
					Bytes.toBytes(qualifer), courseClickCount.getClick_count());
		}
	}

	/**
	 * 根据rowkey查询值
	 * 
	 * @param day_course
	 * @return
	 * @throws IOException
	 */
	public static Long count(String day_course) throws IOException {
		HTable table = HBaseUtils.getInstance().getTable(tableName);
		Get get = new Get(Bytes.toBytes(day_course));
		byte[] value = table.get(get).getValue(cf.getBytes(), qualifer.getBytes());
		if (value == null) {
			return 0L;
		}
		return Bytes.toLong(value);
	}
}
