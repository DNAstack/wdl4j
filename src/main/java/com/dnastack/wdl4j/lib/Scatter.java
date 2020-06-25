package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.api.WorkflowElement;
import com.dnastack.wdl4j.lib.expression.Expression;
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
