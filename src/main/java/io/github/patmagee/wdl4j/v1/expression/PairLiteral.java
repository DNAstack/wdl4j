package io.github.patmagee.wdl4j.v1.expression;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PairLiteral extends Expression {

    private Expression leftValue;
    private Expression rightValue;

}