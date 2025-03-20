package com.isxcode.acorn.api.agent.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlinkRestRunReq {

    private boolean allowNonRestoredState;

    private String entryClass;

    private String jobId;

    private Integer parallelism;

    private String programArgs;

    private List<String> programArgsList;

    private String restoreMode;

    private String savepointPath;

    private Map<String, String> flinkConfiguration;
}
