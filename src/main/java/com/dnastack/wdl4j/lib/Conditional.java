package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.api.WorkflowElement;
import com.dnastack.wdl4j.lib.expression.Expression;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Conditional implements WorkflowElement{

    @NonNull
    private Expression expression;
    private List<WdlElement> elements;
    @NonNull
    private int id;


    public List<Declaration> getDeclarations() {

        return elements == null
               ? null
               : elements.stream()
                         .filter(element -> element instanceof Declaration)
                         .map(element -> (Declaration) element)
                         .collect(Collectors.toList());
    }

    public List<Scatter> getScatters() {
        return elements == null
               ? null
               : elements.stream()
                         .filter(element -> element instanceof Scatter)
                         .map(elements -> (Scatter) elements)
                         .collect(Collectors.toList());
    }

    public List<Conditional> getConditionals() {

        return elements == null
               ? null
               : elements.stream()
                         .filter(element -> element instanceof Conditional)
                         .map(elements -> (Conditional) elements)
                         .collect(Collectors.toList());
    }

    public List<Call> getCalls() {

        return elements == null
               ? null
               : elements.stream()
                         .filter(element -> element instanceof Call)
                         .map(elements -> (Call) elements)
                         .collect(Collectors.toList());
    }
}
