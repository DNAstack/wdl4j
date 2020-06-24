package com.dnastack.wdl4j;

import com.dnastack.wdl4j.api.NamedElement;
import com.dnastack.wdl4j.api.WorkflowElement;
import com.dnastack.wdl4j.expression.Expression;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Call implements WorkflowElement, NamedElement {

    @NonNull
    private String taskName;
    private String callAlias;
    private Map<String, Expression> inputs;
    @NonNull
    private int id;

    @Override
    public String getName() {
        return callAlias != null ? callAlias : taskName;
    }
}
