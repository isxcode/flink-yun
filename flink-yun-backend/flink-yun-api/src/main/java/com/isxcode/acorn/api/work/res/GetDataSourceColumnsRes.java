package com.isxcode.acorn.api.work.res;

import com.isxcode.acorn.api.datasource.dto.ColumnMetaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetDataSourceColumnsRes {

    private List<ColumnMetaDto> columns;
}
