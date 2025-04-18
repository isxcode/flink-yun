package com.isxcode.acorn.api.agent.res;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlinkGetTaskManagerRes {

    @JsonAlias("taskmanagers")
    private List<FlinkTaskManagerRes> taskManagers;
}
