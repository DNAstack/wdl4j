package com.dnastack.wdl4j;

import com.dnastack.wdl4j.api.WdlElement;
import com.dnastack.wdl4j.expression.Expression;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Runtime implements WdlElement {

    private Map<String, Expression> attributes;
    @NonNull
    private int id;

}
