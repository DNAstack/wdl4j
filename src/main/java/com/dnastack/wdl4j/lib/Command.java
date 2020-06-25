package com.dnastack.wdl4j.lib;

import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.expression.Expression;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class Command implements WdlElement {

    private List<CommandPart> commandParts;
    @NonNull
    private int id;

    @Data
    public static class CommandPart implements WdlElement {

        private String stringPart;
        private List<Expression> placeholders;
        private Expression expression;
        @NonNull
        private int id;

        public CommandPart(String stringPart,int id) {
            this.stringPart = stringPart;
            this.placeholders = null;
            this.expression = null;
            this.id = id;
        }

        public CommandPart(List<Expression> placeholders, Expression expression,int id) {
            this.stringPart = null;
            this.placeholders = placeholders;
            this.expression = expression;
            this.id = id;
        }
    }
}
