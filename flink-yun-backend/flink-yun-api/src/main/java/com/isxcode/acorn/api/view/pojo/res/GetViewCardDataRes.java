package com.isxcode.acorn.api.view.pojo.res;

import com.isxcode.acorn.api.view.pojo.dto.EchartOption;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetViewCardDataRes {

    private EchartOption viewData;
}
