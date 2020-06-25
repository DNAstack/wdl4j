package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.expression.Expression;
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
