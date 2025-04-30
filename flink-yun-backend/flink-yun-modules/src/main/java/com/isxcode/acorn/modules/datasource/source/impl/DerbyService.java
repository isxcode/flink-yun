package com.isxcode.acorn.modules.datasource.source.impl;

import com.isxcode.acorn.api.datasource.constants.DatasourceDriver;
import com.isxcode.acorn.api.datasource.constants.DatasourceType;
import com.isxcode.acorn.api.datasource.dto.ConnectInfo;
import com.isxcode.acorn.api.datasource.dto.QueryColumnDto;
import com.isxcode.acorn.api.datasource.dto.QueryTableDto;
import com.isxcode.acorn.api.work.res.GetDataSourceDataRes;
import com.isxcode.acorn.backend.api.base.exceptions.IsxAppException;
import com.isxcode.acorn.backend.api.base.properties.IsxAppProperties;
import com.isxcode.acorn.common.utils.aes.AesUtils;
import com.isxcode.acorn.modules.datasource.service.DatabaseDriverService;
import com.isxcode.acorn.modules.datasource.source.Datasource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class DerbyService extends Datasource {

    public DerbyService(DatabaseDriverService dataDriverService, IsxAppProperties isxAppProperties, AesUtils aesUtils) {
        super(dataDriverService, isxAppProperties, aesUtils);
    }

    @Override
    public String getDataSourceType() {
        return DatasourceType.DERBY;
    }

    @Override
    public String getDriverName() {
        return DatasourceDriver.DERBY_DRIVER;
    }

    @Override
    public List<QueryTableDto> queryTable(ConnectInfo connectInfo) throws IsxAppException {
        Assert.notNull(connectInfo.getDatabase(), "数据库不能为空");

        QueryRunner qr = new QueryRunner();
        try (Connection connection = getConnection(connectInfo)) {
            String schema = connectInfo.getUsername().toUpperCase();

            String sql = "SELECT '" + connectInfo.getDatasourceId()
                + "' AS datasourceId, t.tablename AS tableName, '' AS tableComment " + "FROM SYS.SYSTABLES t "
                + "JOIN SYS.SYSSCHEMAS s ON t.schemaid = s.schemaid " + "WHERE s.schemaname = '" + schema
                + "' and t.TABLETYPE = 'T'";

            if (Strings.isNotEmpty(connectInfo.getTablePattern())) {
                sql += " AND t.tablename LIKE '%" + connectInfo.getTablePattern() + "%'";
            }

            return qr.query(connection, sql, new BeanListHandler<>(QueryTableDto.class));
        } catch (SQLException e) {
            log.error("Error querying tables: {}", e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }

    @Override
    public List<QueryColumnDto> queryColumn(ConnectInfo connectInfo) throws IsxAppException {

        Assert.notNull(connectInfo.getDatabase(), "数据库不能为空");
        Assert.notNull(connectInfo.getTableName(), "表名不能为空");

        QueryRunner qr = new QueryRunner();
        try (Connection connection = getConnection(connectInfo)) {
            String schema = connectInfo.getUsername().toUpperCase();
            String table = connectInfo.getTableName();

            String sql = "SELECT '" + connectInfo.getDatasourceId() + "' AS datasourceId, '" + table
                + "' AS tableName, c.columnname AS columnName, CAST(columndatatype AS VARCHAR(255)) AS columnType, '' AS columnComment, false AS isPartitionColumn "
                + "FROM SYS.SYSCOLUMNS c " + "JOIN SYS.SYSTABLES t ON c.referenceid = t.tableid "
                + "JOIN SYS.SYSSCHEMAS s ON t.schemaid = s.schemaid " + "WHERE s.schemaname = '" + schema
                + "' AND t.tablename = '" + table + "'";

            return qr.query(connection, sql, new BeanListHandler<>(QueryColumnDto.class));
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }

    @Override
    public Long getTableTotalSize(ConnectInfo connectInfo) throws IsxAppException {
        Assert.notNull(connectInfo.getDatabase(), "数据库不能为空");
        Assert.notNull(connectInfo.getTableName(), "表名不能为空");

        String schemaName = connectInfo.getUsername().toUpperCase();

        String sql =
            "SELECT *\n" + "FROM SYS.SYSCONGLOMERATES c\n" + "         JOIN SYS.SYSTABLES t ON c.TABLEID = t.TABLEID\n"
                + "         JOIN SYS.SYSSCHEMAS s ON t.SCHEMAID = s.SCHEMAID\n" + "WHERE s.schemaname = 'ISPONG'\n"
                + "  AND t.tablename = 'USERS'";

        QueryRunner qr = new QueryRunner();
        try (Connection connection = getConnection(connectInfo)) {
            Long size = qr.query(connection, sql, new ScalarHandler<>());
            return size != null ? size : 0L;
        } catch (SQLException e) {
            throw new IsxAppException(e.getMessage());
        }
    }

    @Override
    public Long getTableTotalRows(ConnectInfo connectInfo) throws IsxAppException {
        Assert.notNull(connectInfo.getTableName(), "表名不能为空");

        QueryRunner qr = new QueryRunner();
        try (Connection connection = getConnection(connectInfo)) {
            String sql = "SELECT COUNT(*) FROM \"" + connectInfo.getUsername().toUpperCase() + "\".\""
                + connectInfo.getTableName() + "\"";
            Object query = qr.query(connection, sql, new ScalarHandler<>());
            return Long.parseLong(String.valueOf(query));
        } catch (SQLException e) {
            log.error("Error querying table rows: {}", e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }

    @Override
    public Long getTableColumnCount(ConnectInfo connectInfo) throws IsxAppException {
        Assert.notNull(connectInfo.getTableName(), "表名不能为空");

        QueryRunner qr = new QueryRunner();
        try (Connection connection = getConnection(connectInfo)) {
            String sql = "SELECT COUNT(*) FROM SYS.SYSCOLUMNS c " + "JOIN SYS.SYSTABLES t ON c.referenceid = t.tableid "
                + "JOIN SYS.SYSSCHEMAS s ON t.schemaid = s.schemaid " + "WHERE s.schemaname = '"
                + connectInfo.getUsername().toUpperCase() + "' AND t.tablename = '" + connectInfo.getTableName() + "'";
            Object query = qr.query(connection, sql, new ScalarHandler<>());
            return Long.parseLong(String.valueOf(query));
        } catch (SQLException e) {
            log.error("Error querying table column count: {}", e.getMessage(), e);
            throw new IsxAppException(e.getMessage());
        }
    }

    @Override
    public String getPageSql(String sql) throws IsxAppException {
        return "SELECT * FROM (" + sql + ") AS FY_TMP LIMIT ${pageSize} OFFSET ${page}";
    }

    @Override
    public GetDataSourceDataRes getTableData(ConnectInfo connectInfo) throws IsxAppException {

        Assert.notNull(connectInfo.getTableName(), "tableName不能为空");
        Assert.notNull(connectInfo.getRowNumber(), "rowNumber不能为空");

        String getTableDataSql =
            "SELECT * FROM " + connectInfo.getTableName() + ("ALL".equals(connectInfo.getRowNumber()) ? ""
                : " fetch first " + connectInfo.getRowNumber() + " rows only ");
        return getTableData(connectInfo, getTableDataSql);
    }

    @Override
    public void refreshTableInfo(ConnectInfo connectInfo) throws IsxAppException {
        // Refresh logic can be implemented if needed
    }
}
