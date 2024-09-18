package com.isxcode.acorn.api.agent.pojos.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlinkSubmit {

    private String entryClass;

    private String appResource;

    private String appName;

    private List<String> programArgs;
}
