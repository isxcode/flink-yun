package com.isxcode.acorn.api.agent.res;

import com.isxcode.acorn.api.agent.dto.FlinkSubtaskDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlinkRestVerticesRes {

    private List<FlinkSubtaskDto> subtasks;
}
