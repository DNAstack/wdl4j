package com.dnastack.wdl4j;

import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.api.WorkflowElement;
import com.dnastack.wdl4j.expression.Expression;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Scatter implements WorkflowElement {

    @NonNull
    private String varname;

    @NonNull
    private Expression expression;
    private List<Declaration> declarations;
    private List<WdlElement> workflowElements;
    @NonNull
    private int id;

}
