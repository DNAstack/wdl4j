package com.dnastack.wdl4j.lib.expression.literal;

import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.AbstractExpressionTest;
import com.dnastack.wdl4j.lib.expression.BinaryExpression;
import com.dnastack.wdl4j.lib.expression.Expression;
import com.dnastack.wdl4j.lib.typing.*;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class LiteralExpressionTests extends AbstractExpressionTest {

    @Test
    public void testIntLiteral() throws WdlValidationError {
        Expression expression = evaluateExpression("1");
        assertThat(expression).isInstanceOf(IntLiteral.class);
        IntLiteral literal = (IntLiteral) expression;
        assertThat(literal).extracting("value").isEqualTo(1);

        expression = evaluateExpression("10000");
        assertThat(expression).isInstanceOf(IntLiteral.class);
        literal = (IntLiteral) expression;
        assertThat(literal).extracting("value").isEqualTo(10000);
    }

    @Test
    public void testIntLiteral_Typecheck() throws WdlValidationError {
        Expression expression = evaluateExpression("1");
        assertThat(expression.typeCheck(expression, TEST_NAMESPACE)).isEqualTo(IntType.getType());
    }

    @Test
    public void testFloatLiteral() {
        Expression expression = evaluateExpression("1.0");
        assertThat(expression).isInstanceOf(FloatLiteral.class);
        FloatLiteral literal = (FloatLiteral) expression;
        assertThat(literal).extracting("value").isEqualTo(1.0f);

        expression = evaluateExpression("10000.1231");
        assertThat(expression).isInstanceOf(FloatLiteral.class);
        literal = (FloatLiteral) expression;
        assertThat(literal).extracting("value").isEqualTo(10000.1231f);
    }

    @Test
    public void testFloatLiteral_Typecheck() throws WdlValidationError {
        Expression expression = evaluateExpression("1.0");
        assertThat(expression.typeCheck(expression, TEST_NAMESPACE)).isEqualTo(FloatType.getType());
    }

    @Test
    public void testBooleanLiteral() {
        Expression expression = evaluateExpression("true");
        assertThat(expression).isInstanceOf(BooleanLiteral.class);
        BooleanLiteral literal = (BooleanLiteral) expression;
        assertThat(literal).extracting("value").isEqualTo(true);

        expression = evaluateExpression("false");
        assertThat(expression).isInstanceOf(BooleanLiteral.class);
        literal = (BooleanLiteral) expression;
        assertThat(literal).extracting("value").isEqualTo(false);
    }

    @Test
    public void testBooleanLiteral_Typecheck() throws WdlValidationError {
        Expression expression = evaluateExpression("true");
        assertThat(expression.typeCheck(expression, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());
    }

    @Test
    public void testArrayLiteral() {
        Expression expression = evaluateExpression("[]");
        assertThat(expression).isInstanceOf(ArrayLiteral.class);
        ArrayLiteral literal = (ArrayLiteral) expression;
        assertThat(literal.getValues()).isEmpty();

        literal = (ArrayLiteral) evaluateExpression("[1]");
        assertThat(literal.getValues()).hasSize(1);
        assertThat(literal.getValues().get(0)).isInstanceOf(IntLiteral.class);

        literal = (ArrayLiteral) evaluateExpression("[1,1]");
        assertThat(literal.getValues()).hasSize(2);
        assertThat(literal.getValues().get(0)).isInstanceOf(IntLiteral.class);

        literal = (ArrayLiteral) evaluateExpression("[[1]]");
        assertThat(literal.getValues()).hasSize(1);
        assertThat(literal.getValues().get(0)).isInstanceOf(ArrayLiteral.class);
    }

    @Test
    public void testArrayLiteral_Typecheck() throws WdlValidationError {
        assertThat(evaluateExpression("[1]").typeCheck(null,
                                                       TEST_NAMESPACE)).isEqualTo(ArrayType.getType(IntType.getType()));
        assertThat(evaluateExpression("[[1]]").typeCheck(null,
                                                         TEST_NAMESPACE)).isEqualTo(ArrayType.getType(ArrayType.getType(
                IntType.getType())));
        assertThat(evaluateExpression("[1,'1']").typeCheck(null,
                                                           TEST_NAMESPACE)).isEqualTo(ArrayType.getType(IntType.getType()));
    }

    @Test
    public void testPairLiteral() {
        Expression expression = evaluateExpression("(1,2)");
        assertThat(expression).isInstanceOf(PairLiteral.class);
        PairLiteral literal = (PairLiteral) expression;
        assertThat(literal.getLeftValue()).isInstanceOf(IntLiteral.class).extracting("value").isEqualTo(1);
        assertThat(literal.getRightValue()).isInstanceOf(IntLiteral.class).extracting("value").isEqualTo(2);

        literal = (PairLiteral) evaluateExpression("((1,2),2)");
        assertThat(literal.getLeftValue()).isInstanceOf(PairLiteral.class);
        assertThat(literal.getRightValue()).isInstanceOf(IntLiteral.class);

        literal = (PairLiteral) evaluateExpression("(1,(1,2))");
        assertThat(literal.getLeftValue()).isInstanceOf(IntLiteral.class);
        assertThat(literal.getRightValue()).isInstanceOf(PairLiteral.class);

    }

    @Test
    public void testPairLiteral_Typecheck() throws WdlValidationError {
        assertThat(evaluateExpression("(1,1)").typeCheck(null,
                                                         TEST_NAMESPACE)).isEqualTo(PairType.getType(IntType.getType(),
                                                                                                     IntType.getType()));
        assertThat(evaluateExpression("((1,1),1)").typeCheck(null,
                                                             TEST_NAMESPACE)).isEqualTo(PairType.getType(PairType.getType(
                IntType.getType(),
                IntType.getType()), IntType.getType()));
        assertThat(evaluateExpression("([1,1],1)").typeCheck(null, TEST_NAMESPACE)).isEqualTo(PairType.getType(ArrayType
                                                                                                                       .getType(
                                                                                                                               IntType.getType()),
                                                                                                               IntType.getType()));
    }

    @Test
    public void testMapLiteral() {
        Expression expression = evaluateExpression("{'foo':'bar'}");
        assertThat(expression).isInstanceOf(MapLiteral.class);
        MapLiteral literal = (MapLiteral) expression;
        assertThat(literal).extracting(MapLiteral::getValues).asList().hasSize(1);
        MapLiteral.MapEntry entry = literal.getValues().get(0);
        assertThat(entry.getKey()).isInstanceOf(StringLiteral.class);
        assertThat(entry.getValue()).isInstanceOf(StringLiteral.class);

        literal = (MapLiteral) evaluateExpression("{'foo': ['String','String']}");
        assertThat(literal).extracting(MapLiteral::getValues).asList().hasSize(1);
        entry = literal.getValues().get(0);
        assertThat(entry.getKey()).isInstanceOf(StringLiteral.class);
        assertThat(entry.getValue()).isInstanceOf(ArrayLiteral.class);
    }

    @Test
    public void testMapLiteral_Typecheck() throws WdlValidationError {
        assertThat(evaluateExpression("{1:1}").typeCheck(null,
                                                         TEST_NAMESPACE)).isEqualTo(MapType.getType(IntType.getType(),
                                                                                                    IntType.getType()));
        assertThat(evaluateExpression("{'1':1}").typeCheck(null,
                                                           TEST_NAMESPACE)).isEqualTo(MapType.getType(StringType.getType(),
                                                                                                      IntType.getType()));
        assertThatExceptionOfType(TypeCoercionException.class).isThrownBy(() -> evaluateExpression("{'1':1,{1:2}:123}").typeCheck(
                null,
                TEST_NAMESPACE));

        assertThatExceptionOfType(TypeCoercionException.class).isThrownBy(() -> evaluateExpression("{'1':1,'abc':{1:2}}")
                .typeCheck(null, TEST_NAMESPACE));
        assertThat(evaluateExpression("{1:{'foo':'bar'},2:{}}").typeCheck(null,
                                                                          TEST_NAMESPACE)).isEqualTo(MapType.getType(
                IntType.getType(),
                MapType.getType(StringType.getType(), StringType.getType())));
    }

    @Test
    public void testObjectLiteral() {
        Expression expression = evaluateExpression("object {foo:'bar'}");
        assertThat(expression).isInstanceOf(ObjectLiteral.class);
        ObjectLiteral literal = (ObjectLiteral) expression;
        assertThat(literal.getValues()).containsKeys("foo");
        assertThat(literal.getValues().get("foo")).isInstanceOf(StringLiteral.class);

        literal = (ObjectLiteral) evaluateExpression("object {foo: 'bar', biz: [object {baz:'buzz'}]}");
        assertThat(literal.getValues()).containsKeys("foo", "biz");
        assertThat(literal.getValues().get("biz")).isInstanceOf(ArrayLiteral.class);
        ArrayLiteral arrayLiteral = (ArrayLiteral) literal.getValues().get("biz");
        assertThat(arrayLiteral.getValues().get(0)).isInstanceOf(ObjectLiteral.class);

    }

    @Test
    public void testObjectLiteral_typeCheck_withoutMemberEvaluation() throws WdlValidationError {

        assertThat(evaluateExpression("object {foo:'bar'}").typeCheck(null,
                                                                      TEST_NAMESPACE)).isEqualTo(ObjectType.getType());

    }

    @Test
    public void testStringLiteral() {
        assertThat(evaluateExpression("'foo'")).asInstanceOf(InstanceOfAssertFactories.type(StringLiteral.class))
                                               .extracting(StringLiteral::getStringParts)
                                               .asList()
                                               .hasSize(1)
                                               .first()
                                               .extracting("stringPart")
                                               .isEqualTo("foo");
        assertThat(evaluateExpression("\"foo\"")).asInstanceOf(InstanceOfAssertFactories.type(StringLiteral.class))
                                                 .extracting(StringLiteral::getStringParts)
                                                 .asList()
                                                 .hasSize(1)
                                                 .first()
                                                 .extracting("stringPart")
                                                 .isEqualTo("foo");

        assertThat(evaluateExpression("'foo.~{1 + 2}'")).asInstanceOf(InstanceOfAssertFactories.type(StringLiteral.class))
                                                        .extracting(StringLiteral::getStringParts)
                                                        .asList()
                                                        .hasSize(3)
                                                        .element(1)
                                                        .extracting("expression")
                                                        .isInstanceOf(BinaryExpression.class);

        assertThat(evaluateExpression("'foo.~{1 + 2}.~{2 + 2}'")).asInstanceOf(InstanceOfAssertFactories.type(
                StringLiteral.class))
                                                                 .extracting(StringLiteral::getStringParts)
                                                                 .asList()
                                                                 .hasSize(5)
                                                                 .element(3)
                                                                 .extracting("expression")
                                                                 .isInstanceOf(BinaryExpression.class);

    }

    @Test
    public void testStringLiteral_Typecheck() throws WdlValidationError {
        assertThat(evaluateExpression("'foo'").typeCheck(null, TEST_NAMESPACE)).isEqualTo(StringType.getType());
        assertThat(evaluateExpression("'foo-~{1}'").typeCheck(null, TEST_NAMESPACE)).isEqualTo(StringType.getType());

        assertThat(evaluateExpression("'foo-~{1+1}'").typeCheck(null, TEST_NAMESPACE)).isEqualTo(StringType.getType());
        //Should not work
        assertThatExceptionOfType(TypeCoercionException.class).isThrownBy(() -> evaluateExpression("'foo-~{{1:2}}'").typeCheck(
                null,
                TEST_NAMESPACE));

    }

}
