package com.isxcode.acorn.api.view.res;

import com.isxcode.acorn.api.view.dto.CardInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetViewRes {

    private String id;

    private String name;

    private String status;

    private Object webConfig;

    private List<CardInfo> cards;
}
