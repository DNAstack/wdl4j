package com.dnastack.wdl4j.lib.expression;

import com.dnastack.wdl4j.WdlV1DocumentVisitor;
import com.dnastack.wdl4j.lib.Declaration;
import com.dnastack.wdl4j.lib.LanguageLevel;
import com.dnastack.wdl4j.lib.Namespace;
import com.dnastack.wdl4j.lib.Struct;
import com.dnastack.wdl4j.lib.exception.NamespaceException;
import com.dnastack.wdl4j.lib.stdlib.WdlV1StandardLib;
import com.dnastack.wdl4j.lib.typing.*;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.BeforeAll;
import org.openwdl.wdl.parser.WdlV1Lexer;
import org.openwdl.wdl.parser.WdlV1Parser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class AbstractExpressionTest {

    public static Namespace TEST_NAMESPACE = null;

    public WdlV1Parser getV1Parser(String inp) {
        CodePointBuffer codePointBuffer = CodePointBuffer.withBytes(ByteBuffer.wrap(inp.getBytes()));
        WdlV1Lexer lexer = new WdlV1Lexer(CodePointCharStream.fromBuffer(codePointBuffer));
        return new WdlV1Parser(new CommonTokenStream(lexer));
    }

    public Expression evaluateExpression(String inp) {
        WdlV1Parser parser = getV1Parser(inp);
        WdlV1DocumentVisitor visitor = new WdlV1DocumentVisitor();
        return visitor.visitExpr(parser.expr());
    }

    @BeforeAll
    public static void init() throws NamespaceException {
        TEST_NAMESPACE = new Namespace();
        TEST_NAMESPACE.setCoercionOptions(new CoercionOptions(LanguageLevel.WDL_V1));
        TEST_NAMESPACE.setLib(new WdlV1StandardLib());
        TEST_NAMESPACE.addDeclaration("string", new Declaration(StringType.getType(), "string", null, 1));
        TEST_NAMESPACE.addDeclaration("int", new Declaration(IntType.getType(), "int", null, 2));
        TEST_NAMESPACE.addDeclaration("float", new Declaration(FloatType.getType(), "float", null, 3));
        TEST_NAMESPACE.addDeclaration("bool", new Declaration(BooleanType.getType(), "bool", null, 4));
        TEST_NAMESPACE.addDeclaration("file", new Declaration(FileType.getType(), "file", null, 5));
        TEST_NAMESPACE.addDeclaration("string_opt",
                                      new Declaration(OptionalType.getType(StringType.getType()),
                                                      "string_opt",
                                                      null,
                                                      6));
        TEST_NAMESPACE.addDeclaration("int_opt",
                                      new Declaration(OptionalType.getType(IntType.getType()), "int_opt", null, 7));
        TEST_NAMESPACE.addDeclaration("float_opt",
                                      new Declaration(OptionalType.getType(FloatType.getType()), "float_opt", null, 8));
        TEST_NAMESPACE.addDeclaration("bool_opt",
                                      new Declaration(OptionalType.getType(BooleanType.getType()),
                                                      "bool_opt",
                                                      null,
                                                      9));
        TEST_NAMESPACE.addDeclaration("file_opt",
                                      new Declaration(OptionalType.getType(FileType.getType()), "file_opt", null, 10));
    TEST_NAMESPACE.addDeclaration("array_file_opt",new Declaration(OptionalType.getType(ArrayType.getType(FileType.getType())), "array_file_opt", null, 223));

        List<Declaration> members = new ArrayList<>();
        members.add(new Declaration(StringType.getType(), "string", null, 11));
        members.add(new Declaration(IntType.getType(), "int", null, 12));
        members.add(new Declaration(FloatType.getType(), "float", null, 13));
        members.add(new Declaration(BooleanType.getType(), "bool", null, 14));
        members.add(new Declaration(FileType.getType(), "file", null, 15));
        members.add(new Declaration(ArrayType.getType(FileType.getType()), "array_file", null, 16));
        members.add(new Declaration(OptionalType.getType(ArrayType.getType(FileType.getType())), "array_file_opt", null, 16));
        TEST_NAMESPACE.addStruct(new Struct("struct", members, 17));

    }

}
