package com.isxcode.acorn.api.real.res;

import com.isxcode.acorn.api.work.dto.SyncWorkConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRealRes {

    private String id;

    private List<String> libConfig;

    private List<String> funcConfig;

    private String flinkConfig;

    private String clusterId;

    private SyncWorkConfig syncConfig;
}
