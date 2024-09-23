package com.isxcode.acorn.modules.datasource.source.impl;

import com.isxcode.acorn.api.datasource.constants.DatasourceDriver;
import com.isxcode.acorn.api.datasource.constants.DatasourceType;
import com.isxcode.acorn.api.datasource.pojos.dto.ConnectInfo;
import com.isxcode.acorn.api.datasource.pojos.dto.QueryColumnDto;
import com.isxcode.acorn.api.datasource.pojos.dto.QueryTableDto;
import com.isxcode.acorn.api.work.pojos.res.GetDataSourceDataRes;
import com.isxcode.acorn.backend.api.base.properties.IsxAppProperties;
import com.isxcode.acorn.common.utils.AesUtils;
import com.isxcode.acorn.modules.datasource.service.DatabaseDriverService;
import com.isxcode.acorn.modules.datasource.source.Datasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Db2Service extends Datasource {

    public Db2Service(DatabaseDriverService dataDriverService, IsxAppProperties isxAppProperties, AesUtils aesUtils) {
        super(dataDriverService, isxAppProperties, aesUtils);
    }

    @Override
    public String getDataSourceType() {
        return DatasourceType.DB2;
    }

    @Override
    public String getDriverName() {
        return DatasourceDriver.DB2_DRIVER;
    }

    @Override
    public List<QueryTableDto> queryTable(ConnectInfo connectInfo) {
        throw new RuntimeException("该数据源暂不支持");
    }

    @Override
    public List<QueryColumnDto> queryColumn(ConnectInfo connectInfo) {
        throw new RuntimeException("该数据源暂不支持");
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
    public GetDataSourceDataRes getTableData(ConnectInfo connectInfo) {
        return null;
    }

    @Override
    public void refreshTableInfo(ConnectInfo connectInfo) {

    }

}
