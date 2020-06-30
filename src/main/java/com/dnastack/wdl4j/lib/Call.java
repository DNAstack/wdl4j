package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.NamedElement;
import com.dnastack.wdl4j.lib.api.WorkflowElement;
import com.dnastack.wdl4j.lib.expression.Expression;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Call implements WorkflowElement, NamedElement {

    @NonNull
    private String taskOrWorkflowName;
    private String callAlias;
    private Map<String, Expression> inputs;

    private Map<String, Declaration> effectiveCallInputs;
    private Map<String, Declaration> effectiveCallOutputs;

    @NonNull
    private int id;

    @Override
    public String getName() {
        return callAlias != null ? callAlias : taskOrWorkflowName;
    }
}
