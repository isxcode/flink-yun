package com.isxcode.acorn.api.view.pojo.res;

import com.isxcode.acorn.api.view.pojo.dto.CardInfo;
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
