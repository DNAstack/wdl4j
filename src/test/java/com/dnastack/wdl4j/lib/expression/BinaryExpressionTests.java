package com.dnastack.wdl4j.lib.expression;

import com.dnastack.wdl4j.lib.exception.TypeCoercionException;
import com.dnastack.wdl4j.lib.exception.WdlValidationError;
import com.dnastack.wdl4j.lib.expression.literal.*;
import com.dnastack.wdl4j.lib.typing.BooleanType;
import com.dnastack.wdl4j.lib.typing.FloatType;
import com.dnastack.wdl4j.lib.typing.IntType;
import com.dnastack.wdl4j.lib.typing.StringType;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BinaryExpressionTests extends AbstractExpressionTest {

    @Test
    public void testBinaryExpression_add() throws WdlValidationError {
        BinaryExpression binaryExpression = (BinaryExpression) evaluateExpression("1 + 1");
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.ADD);
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1.0 + 1.0");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(FloatLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(FloatLiteral.class);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(FloatType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("'a' + 'a'");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(StringLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(StringLiteral.class);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(StringType.getType());

    }

    @Test
    public void testBinaryExpression_add_StringInExpression_CausesEvalToString() throws WdlValidationError {
        BinaryExpression binaryExpression = (BinaryExpression) evaluateExpression("1 + '1'");
        AssertionsForClassTypes.assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        AssertionsForClassTypes.assertThat(binaryExpression.getRightHandSide()).isInstanceOf(StringLiteral.class);
        AssertionsForClassTypes.assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE))
                               .isEqualTo(StringType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("'1' + 1");
        AssertionsForClassTypes.assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(StringLiteral.class);
        AssertionsForClassTypes.assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        AssertionsForClassTypes.assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE))
                               .isEqualTo(StringType.getType());
    }

    @Test
    public void testBinaryExpression_numericOperators() throws WdlValidationError {
        BinaryExpression binaryExpression = (BinaryExpression) evaluateExpression("1 / 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.DIVIDE);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());
        assertThatExceptionOfType(TypeCoercionException.class).isThrownBy(() -> evaluateExpression("1 / true").typeCheck(
                null,
                TEST_NAMESPACE));

        binaryExpression = (BinaryExpression) evaluateExpression("1 - 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.SUBTRACT);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1 * 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.MULTIPLY);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1 % 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.MOD);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());
    }

    @Test
    public void testBinaryExpression_equalityOperators() throws WdlValidationError {
        BinaryExpression binaryExpression = (BinaryExpression) evaluateExpression("1 == 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.EQUAL_TO);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1 != 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.NOT_EQUAL_TO);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("[[[1]],[[2]]] == 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(ArrayLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.EQUAL_TO);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());
        binaryExpression = (BinaryExpression) evaluateExpression("1 != [[[1]],[[2]]]");
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(ArrayLiteral.class);
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.NOT_EQUAL_TO);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());
    }

    @Test
    public void testBinaryExpression_greaterThanAndLessThanOperators() throws WdlValidationError {
        BinaryExpression binaryExpression = (BinaryExpression) evaluateExpression("1 > 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.GREATER_THAN);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1 >= 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.GREATER_THAN_OR_EQUAL);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1 < 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.LESS_THAN);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1 <= 1");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.LESS_THAN_OR_EQUAL);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("'1' > '1'");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(StringLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(StringLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.GREATER_THAN);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

    }

    @Test
    public void testBinaryExpression_logicalOperators() throws WdlValidationError {
        BinaryExpression binaryExpression = (BinaryExpression) evaluateExpression("true ||  false");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(BooleanLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(BooleanLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.LOGICAL_OR);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        assertThatExceptionOfType(TypeCoercionException.class).isThrownBy(() -> evaluateExpression("true ||  1").typeCheck(
                null,
                TEST_NAMESPACE))
                                                              .withMessageContaining(
                                                                      "Illegal Type coercion. Left hand side of binary operation LOGICAL_OR cannot be cast to a boolean type");

        binaryExpression = (BinaryExpression) evaluateExpression("true &&  false");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(BooleanLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(BooleanLiteral.class);
        assertThat(binaryExpression.getOperation()).isEqualTo(BinaryExpression.BinaryOperation.LOGICAL_AND);
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(BooleanType.getType());

        assertThatExceptionOfType(TypeCoercionException.class).isThrownBy(() -> evaluateExpression("true &&  1").typeCheck(
                null,
                TEST_NAMESPACE))
                                                              .withMessageContaining(
                                                                      "Illegal Type coercion. Left hand side of binary operation LOGICAL_AND cannot be cast to a boolean type");

    }

    @Test
    public void testNestedBinaryExpression() throws WdlValidationError {
        BinaryExpression binaryExpression = (BinaryExpression) evaluateExpression("1 + 2 + 3");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(BinaryExpression.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getLeftHandSide().typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());

        binaryExpression = (BinaryExpression) evaluateExpression("1 + 2 * 3");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(IntLiteral.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(BinaryExpression.class);
        assertThat(binaryExpression.getRightHandSide().typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());


        binaryExpression = (BinaryExpression) evaluateExpression("1 + 2 + '3'");
        assertThat(binaryExpression.getLeftHandSide()).isInstanceOf(BinaryExpression.class);
        assertThat(binaryExpression.getRightHandSide()).isInstanceOf(StringLiteral.class);
        assertThat(binaryExpression.getLeftHandSide().typeCheck(null, TEST_NAMESPACE)).isEqualTo(IntType.getType());
        assertThat(binaryExpression.typeCheck(null, TEST_NAMESPACE)).isEqualTo(StringType.getType());

    }
}
