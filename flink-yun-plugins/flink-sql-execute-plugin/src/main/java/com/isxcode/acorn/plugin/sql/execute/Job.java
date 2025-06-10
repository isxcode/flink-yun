package com.isxcode.acorn.plugin.sql.execute;

import com.alibaba.fastjson2.JSON;
import com.isxcode.acorn.api.agent.req.PluginReq;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.UserDefinedFunction;
import org.apache.logging.log4j.util.Strings;

import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

public class Job {

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException,
        InvocationTargetException, InstantiationException, IllegalAccessException {

        PluginReq acornPluginReq = JSON.parseObject(Base64.getDecoder().decode(args[0]), PluginReq.class);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(env, settings);

        // 导入自定义函数
        if (acornPluginReq.getFuncInfoList() != null) {
            for (int i = 0; i < acornPluginReq.getFuncInfoList().size(); i++) {
                Class<?> clazz = Class.forName(acornPluginReq.getFuncInfoList().get(i).getClassName());
                UserDefinedFunction functionInstance =
                    (UserDefinedFunction) clazz.getDeclaredConstructor().newInstance();
                streamTableEnvironment.createTemporarySystemFunction(
                    acornPluginReq.getFuncInfoList().get(i).getFuncName(), functionInstance);
            }
        }

        // 拆分sql
        String[] sqlList = acornPluginReq.getSql().split("\\);");
        for (String s : sqlList) {
            String sql = s;
            if (!sql.endsWith(")") && !sql.endsWith(";")) {
                sql = sql + ")";
            }
            streamTableEnvironment.executeSql(sql);
        }
    }
}
