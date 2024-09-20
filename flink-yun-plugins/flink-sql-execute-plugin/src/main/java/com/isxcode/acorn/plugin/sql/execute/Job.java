package com.isxcode.acorn.plugin.sql.execute;

import com.alibaba.fastjson2.JSON;
import com.isxcode.acorn.api.agent.pojos.req.PluginReq;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.Base64;

public class Job {

    public static void main(String[] args) {

        PluginReq acornPluginReq = JSON.parseObject(Base64.getDecoder().decode(args[0]), PluginReq.class);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(env, settings);

        for (String s : acornPluginReq.getSql().split(";")) {
            streamTableEnvironment.executeSql(s);
        }
    }
}
