package com.isxcode.acorn.agent.run;

import com.isxcode.acorn.api.agent.pojos.req.GetJobInfoReq;
import com.isxcode.acorn.api.agent.pojos.req.GetJobLogReq;
import com.isxcode.acorn.api.agent.pojos.req.StopJobReq;
import com.isxcode.acorn.api.agent.pojos.req.SubmitJobReq;
import com.isxcode.acorn.api.agent.pojos.res.GetJobInfoRes;
import com.isxcode.acorn.api.agent.pojos.res.GetJobLogRes;
import com.isxcode.acorn.api.agent.pojos.res.StopJobRes;
import com.isxcode.acorn.api.agent.pojos.res.SubmitJobRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptionsInternal;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class YarnAgentAcorn implements AcornRun {


    @Override
    public SubmitJobRes submitJob(SubmitJobReq submitJobReq) {

        return null;
    }

    @Override
    public GetJobInfoRes getJobInfo(GetJobInfoReq getJobInfoReq) {
        return null;
    }

    @Override
    public GetJobLogRes getJobLog(GetJobLogReq getJobLogReq) {
        return null;
    }

    @Override
    public StopJobRes stopJobReq(StopJobReq stopJobReq) {
        return null;
    }

//    public static void main(String[] args) {
//        String flinkSql = "CREATE TABLE from_table(\n" +
//            "    username STRING,\n" +
//            "    age INT\n" +
//            ") WITH (\n" +
//            "    'connector'='jdbc',\n" +
//            "    'url'='jdbc:mysql://localhost:30306/ispong_db',\n" +
//            "    'table-name'='users',\n" +
//            "    'driver'='com.mysql.cj.jdbc.Driver',\n" +
//            "    'username'='root',\n" +
//            "    'password'='ispong123');" +
//            "CREATE TABLE to_table(\n" +
//            "    username STRING,\n" +
//            "    age INT\n" +
//            ") WITH (\n" +
//            "    'connector'='jdbc',\n" +
//            "    'url'='jdbc:mysql://localhost:30306/ispong_db',\n" +
//            "    'table-name'='users2',\n" +
//            "    'driver'='com.mysql.cj.jdbc.Driver',\n" +
//            "    'username'='root',\n" +
//            "    'password'='ispong123');" +
//            "insert into to_table select * from from_table";
//        System.out.println(Base64.getEncoder().encodeToString(flinkSql.getBytes()));
//    }
}
