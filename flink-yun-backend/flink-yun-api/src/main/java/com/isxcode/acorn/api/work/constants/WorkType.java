package com.isxcode.acorn.api.work.constants;

/**
 * 作业类型.
 */
public interface WorkType {

    /**
     * flinkSql执行作业.
     */
    String FLINK_SQL = "FLINK_SQL";

    /**
     * jdbcSql执行作业
     */
    String EXECUTE_JDBC_SQL = "EXE_JDBC";

    /**
     * jdbcSql查询作业.
     */
    String QUERY_JDBC_SQL = "QUERY_JDBC";

    /**
     * 数据同步作业.
     */
    String DATA_SYNC_JDBC = "DATA_SYNC_JDBC";

    /**
     * bash脚本作业.
     */
    String BASH = "BASH";

    /**
     * python脚本作业.
     */
    String PYTHON = "PYTHON";

    /**
     * 用户自定义flink作业.
     */
    String FLINK_JAR = "FLINK_JAR";

    /**
     * flink容器sql.
     */
    String FLINK_CONTAINER_SQL = "FLINK_CONTAINER_SQL";

    String API = "API";

    String CURL = "CURL";

    /**
     * Excel导入作业.
     */
    String EXCEL_SYNC_JDBC = "EXCEL_SYNC_JDBC";

    /**
     * 实时计算.
     */
    String REAL_WORK = "REAL_WORK";
}
