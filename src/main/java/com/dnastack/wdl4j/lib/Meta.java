package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.WdlElement;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Meta implements WdlElement {

    private Map<String, Object> attributes;
    @NonNull
    private int id;

}
