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
public class Conditional implements WorkflowElement {

    @NonNull
    private Expression expression;
    private List<WdlElement> elements;
    @NonNull
    private int id;

}
