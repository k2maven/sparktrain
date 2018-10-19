package com.imooc.spark.Project.utils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseUtils {
	public static HBaseUtils instance = null;
	private HBaseAdmin admin = null;
	private Configuration configuration = null;

	private HBaseUtils() {
		configuration = new Configuration();
		configuration.set("hbase.zookeeper.quorum", "localhost:2181");
		configuration.set("hbase.rootdir", "hdfs://localhost:8020/hbase");
		try {
			admin = new HBaseAdmin(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static synchronized HBaseUtils getInstance() {
		if (instance == null) {
			instance = new HBaseUtils();
		}
		return instance;
	}

	/**
	 * 根据表名获取HTable实例
	 * 
	 * @param tableName
	 * @return
	 */
	public HTable getTable(String tableName) {
		HTable table = null;
		try {
			table = new HTable(configuration, tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return table;
	}

	/**
	 * 添加一条记录到HBase表
	 * 
	 * @param tableName HBase表名
	 * @param rowkey    HBase表的rowKey
	 * @param cf        HBase表的columnfamily
	 * @param column    HBase表的column
	 * @param value     column的value
	 */
	public void put(String tableName, String rowkey, String cf, String column, String value) {
		HTable table = getTable(tableName);
		Put put = new Put(Bytes.toBytes(rowkey));
		put.add(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));
		try {
			table.put(put);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		HTable table = HBaseUtils.getInstance().getTable("imooc_course_clickcount");
		System.out.println(table.getName().getNameAsString());
	}
}
