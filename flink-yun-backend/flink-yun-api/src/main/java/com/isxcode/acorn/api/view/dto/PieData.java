package com.isxcode.acorn.api.view.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PieData {

    private String name;

    private Double value;
}
