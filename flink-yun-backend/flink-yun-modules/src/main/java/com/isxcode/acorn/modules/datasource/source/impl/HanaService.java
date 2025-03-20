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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class HanaService extends Datasource {

    public HanaService(DatabaseDriverService dataDriverService, IsxAppProperties isxAppProperties, AesUtils aesUtils) {
        super(dataDriverService, isxAppProperties, aesUtils);
    }

    @Override
    public String getDataSourceType() {
        return DatasourceType.HANA_SAP;
    }

    @Override
    public String getDriverName() {
        return DatasourceDriver.HANA_SAP_DRIVER;
    }

    @Override
    public List<QueryTableDto> queryTable(ConnectInfo connectInfo) {
        return Collections.emptyList();
    }

    @Override
    public List<QueryColumnDto> queryColumn(ConnectInfo connectInfo) {
        return Collections.emptyList();
    }

    @Override
    public Long getTableTotalSize(ConnectInfo connectInfo) {
        return 0L;
    }

    @Override
    public Long getTableTotalRows(ConnectInfo connectInfo) {
        return 0L;
    }

    @Override
    public Long getTableColumnCount(ConnectInfo connectInfo) {
        return 0L;
    }

    @Override
    public String getPageSql(String sql) throws IsxAppException {
        return "";
    }

    @Override
    public GetDataSourceDataRes getTableData(ConnectInfo connectInfo) {
        return null;
    }

    @Override
    public void refreshTableInfo(ConnectInfo connectInfo) {

    }
}
