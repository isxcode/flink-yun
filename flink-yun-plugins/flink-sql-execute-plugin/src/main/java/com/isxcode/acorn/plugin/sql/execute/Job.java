package com.isxcode.acorn.plugin.sql.execute;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Base64;

public class Job {

	public static void main(String[] args) {

		String flinkSql = new String(Base64.getDecoder().decode(args[0]));

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
		StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(env, settings);

		for (String s : flinkSql.split(";")) {
			streamTableEnvironment.executeSql(s);
		}
	}
}
