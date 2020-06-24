package com.dnastack.wdl4j;

import com.dnastack.wdl4j.api.WdlElement;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Version implements WdlElement {

    private String release;
    @NonNull
    private int id;

}
