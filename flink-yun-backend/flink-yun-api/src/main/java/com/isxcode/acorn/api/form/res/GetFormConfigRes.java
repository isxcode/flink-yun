package com.isxcode.acorn.api.form.res;

import com.isxcode.acorn.api.form.dto.FormComponentDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetFormConfigRes {

    private String formId;

    private List<FormComponentDto> components;

    private String datasourceId;

    private String status;

    private String mainTable;

    private String formVersion;
}
