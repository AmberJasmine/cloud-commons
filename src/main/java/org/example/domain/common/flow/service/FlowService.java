package org.example.domain.common.flow.service;


import org.example.until.Result;

import java.util.List;

public interface FlowService {

    Result createFlow(String flowFlag, List<String> flowApprovers);

}
