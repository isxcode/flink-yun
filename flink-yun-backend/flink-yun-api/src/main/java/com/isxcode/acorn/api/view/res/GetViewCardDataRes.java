package com.isxcode.acorn.api.view.res;

import com.isxcode.acorn.api.view.dto.EchartOption;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetViewCardDataRes {

    private EchartOption viewData;
}
