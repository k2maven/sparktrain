package com.imooc.spark.Project.Spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.imooc.spark.Project.dao.CourseClickCountDAO;
import com.imooc.spark.Project.dao.CourseSearchClickCountDAO;
import com.imooc.spark.Project.domin.ClickLog;
import com.imooc.spark.Project.domin.CourseClickCount;
import com.imooc.spark.Project.domin.CourseSearchClick;
import com.imooc.spark.Project.utils.DateUtils;

import scala.Tuple2;


public class ImoocStatStreaming {
	public static void main(String[] args) {
		if (args.length != 4) {
			// args = localhost:2181 test streamingtopic 1
			System.err.println("Usage:KafKaReceiverWordCount <zkQuorum> <group> <topics> <numThreads>");
			System.exit(1);
		}
		SparkConf conf = new SparkConf().setAppName("ImoocStatStreaming").setMaster("local[2]");
		JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(5000));
		String zkQuorum = args[0];
		String group = args[1];
		String topics = args[2];
		String numThreads = args[3];
		Iterator<String> iterator = Arrays.asList(topics.split(",")).iterator();
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			topicMap.put(string, Integer.valueOf(numThreads));
		}
		JavaPairReceiverInputDStream<String, String> messge = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap);

		// 数据清洗,格式(ip,time,courseId,statusCode,referer)
		JavaDStream<String> logs = messge.map(x -> x._2);

		JavaDStream<ClickLog> cleanLogs = logs.map(x -> {
			String[] infos = x.split("\t");
			String url = infos[2].split(" ")[1];
			Integer courseId = 0;
			if (url.startsWith("/class")) {
				String courseIdHtml = url.split("/")[2];
				courseId = Integer.valueOf(courseIdHtml.substring(0, courseIdHtml.lastIndexOf(".")));
			}
			return new ClickLog(infos[0], DateUtils.parseToMinute(infos[1]), courseId, Integer.valueOf(infos[3]),
					infos[4]);
		}).filter(x -> x.getCourseId() != 0).cache();
		// 功能一：统计今天到现在为止实战课程的访问量

		cleanLogs.mapToPair(x -> {
			return new Tuple2<String, Integer>(x.getTime().substring(0, 8) + "_" + x.getCourseId(), 1);
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		}).foreachRDD(rdd -> {
			rdd.foreachPartition(partitionRecords -> {
				List<CourseClickCount> list = new ArrayList<>();
				while (partitionRecords.hasNext()) {
					Tuple2<String, Integer> pair = partitionRecords.next();
					list.add(new CourseClickCount(pair._1, Long.valueOf(pair._2)));
				}
				CourseClickCountDAO.save(list);
			});
		});

		// 功能二：统计从搜索引擎过来的今天到现在为止实战课程的访问量
		cleanLogs.map(x -> {
			String referer = x.getReferer().replaceAll("//", "/");
			String[] splits = referer.split("/");
			String host = "";
			if (splits.length > 2) {
				host = splits[1];
			}
			return RowFactory.create(host, x.getCourseId(), x.getTime());
		}).filter(row -> row.get(0) != "").mapToPair(x -> {
			return new Tuple2<String, Integer>((x.getString(2)).substring(0, 8) + "_" + x.get(0) + "_" + x.get(1), 1);
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {

			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		}).foreachRDD(rdd -> {
			rdd.foreachPartition(partitionRecords -> {
				List<CourseSearchClick> list = new ArrayList<>();
				while (partitionRecords.hasNext()) {
					Tuple2<String, Integer> pair = partitionRecords.next();
					list.add(new CourseSearchClick(pair._1, Long.valueOf(pair._2)));
				}
				CourseSearchClickCountDAO.save(list);
			});
		});
	}
}
