package com.dnastack.wdl4j;

import com.dnastack.wdl4j.lib.Runtime;
import com.dnastack.wdl4j.lib.*;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.expression.*;
import com.dnastack.wdl4j.lib.expression.literal.*;
import com.dnastack.wdl4j.lib.stdlib.WdlV1StandardLib;
import com.dnastack.wdl4j.lib.typing.*;
import org.openwdl.wdl.parser.WdlV1Parser;
import org.openwdl.wdl.parser.WdlV1ParserBaseVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WdlV1DocumentVisitor extends WdlV1ParserBaseVisitor<WdlElement> {

    private AtomicInteger idCounter = new AtomicInteger(0);

    private int getNextId() {
        return idCounter.addAndGet(1);
    }

    private Expression visitExpr_infix0(WdlV1Parser.Expr_infix0Context ctx) {
        if (ctx instanceof WdlV1Parser.LorContext) {
            return visitLor((WdlV1Parser.LorContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Infix1Context) {
            return (Expression) visitInfix1((WdlV1Parser.Infix1Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix1(WdlV1Parser.Expr_infix1Context ctx) {
        if (ctx instanceof WdlV1Parser.LandContext) {
            return visitLand((WdlV1Parser.LandContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Infix2Context) {
            return (Expression) visitInfix2((WdlV1Parser.Infix2Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix2(WdlV1Parser.Expr_infix2Context ctx) {
        if (ctx instanceof WdlV1Parser.EqeqContext) {
            return visitEqeq((WdlV1Parser.EqeqContext) ctx);
        } else if (ctx instanceof WdlV1Parser.GteContext) {
            return visitGte((WdlV1Parser.GteContext) ctx);
        } else if (ctx instanceof WdlV1Parser.GtContext) {
            return visitGt((WdlV1Parser.GtContext) ctx);
        } else if (ctx instanceof WdlV1Parser.LtContext) {
            return visitLt((WdlV1Parser.LtContext) ctx);
        } else if (ctx instanceof WdlV1Parser.LteContext) {
            return visitLte((WdlV1Parser.LteContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Infix3Context) {
            return (Expression) visitInfix3((WdlV1Parser.Infix3Context) ctx);
        } else if (ctx instanceof WdlV1Parser.NeqContext) {
            return visitNeq((WdlV1Parser.NeqContext) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix3(WdlV1Parser.Expr_infix3Context ctx) {
        if (ctx instanceof WdlV1Parser.AddContext) {
            return visitAdd((WdlV1Parser.AddContext) ctx);
        } else if (ctx instanceof WdlV1Parser.SubContext) {
            return visitSub((WdlV1Parser.SubContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Infix4Context) {
            return (Expression) visitInfix4((WdlV1Parser.Infix4Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix4(WdlV1Parser.Expr_infix4Context ctx) {
        if (ctx instanceof WdlV1Parser.ModContext) {
            return visitMod((WdlV1Parser.ModContext) ctx);
        } else if (ctx instanceof WdlV1Parser.MulContext) {
            return visitMul((WdlV1Parser.MulContext) ctx);
        } else if (ctx instanceof WdlV1Parser.DivideContext) {
            return visitDivide((WdlV1Parser.DivideContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Infix5Context) {
            return (Expression) visitInfix5((WdlV1Parser.Infix5Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpressionCore(WdlV1Parser.Expr_coreContext ctx) {
        if (ctx instanceof WdlV1Parser.ApplyContext) {
            return visitApply((WdlV1Parser.ApplyContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Array_literalContext) {
            return visitArray_literal((WdlV1Parser.Array_literalContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Pair_literalContext) {
            return visitPair_literal((WdlV1Parser.Pair_literalContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Map_literalContext) {
            return visitMap_literal((WdlV1Parser.Map_literalContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Object_literalContext) {
            return visitObject_literal((WdlV1Parser.Object_literalContext) ctx);
        } else if (ctx instanceof WdlV1Parser.IfthenelseContext) {
            return visitIfthenelse((WdlV1Parser.IfthenelseContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Expression_groupContext) {
            return visitExpression_group((WdlV1Parser.Expression_groupContext) ctx);
        } else if (ctx instanceof WdlV1Parser.AtContext) {
            return visitAt((WdlV1Parser.AtContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Get_nameContext) {
            return visitGet_name((WdlV1Parser.Get_nameContext) ctx);
        } else if (ctx instanceof WdlV1Parser.NegateContext) {
            return visitNegate((WdlV1Parser.NegateContext) ctx);
        } else if (ctx instanceof WdlV1Parser.UnarysignedContext) {
            return (Expression) visitUnarysigned((WdlV1Parser.UnarysignedContext) ctx);
        } else if (ctx instanceof WdlV1Parser.PrimitivesContext) {
            return visitPrimitives((WdlV1Parser.PrimitivesContext) ctx);
        } else if (ctx instanceof WdlV1Parser.Left_nameContext) {
            return visitLeft_name((WdlV1Parser.Left_nameContext) ctx);
        } else {
            return null;
        }
    }

    private Object visitMetaValue(WdlV1Parser.Meta_valueContext meta_valueContext) {
        if (meta_valueContext.MetaBool() != null) {
            return Boolean.parseBoolean(meta_valueContext.MetaBool().getText());
        } else if (meta_valueContext.MetaFloat() != null) {
            return Float.parseFloat(meta_valueContext.MetaFloat().getText());
        } else if (meta_valueContext.MetaInt() != null) {
            return Float.parseFloat(meta_valueContext.MetaInt().getText());
        } else if (meta_valueContext.meta_string() != null) {
            return meta_valueContext.meta_string().meta_string_part().getText();
        } else if (meta_valueContext.meta_object() != null) {
            WdlV1Parser.Meta_objectContext metaObject = meta_valueContext.meta_object();
            Map<String, Object> metaMap = new HashMap<>();
            for (WdlV1Parser.Meta_object_kvContext metaObjectKvContext : metaObject.meta_object_kv()) {
                metaMap.put(metaObjectKvContext.MetaObjectIdentifier().getText(),
                            visitMetaValue(metaObjectKvContext.meta_value()));
            }
            return metaMap;
        } else if (meta_valueContext.meta_array() != null) {
            WdlV1Parser.Meta_arrayContext metaArrayContext = meta_valueContext.meta_array();
            List<Object> metaList = new ArrayList<>();
            for (WdlV1Parser.Meta_valueContext metaValue : metaArrayContext.meta_value()) {
                metaList.add(visitMetaValue(metaValue));
            }
            return metaList;
        } else {
            return null;
        }
    }

    @Override
    public Type visitType_base(WdlV1Parser.Type_baseContext ctx) {
        if (ctx.BOOLEAN() != null) {
            return BooleanType.getType();
        } else if (ctx.STRING() != null) {
            return StringType.getType();
        } else if (ctx.FILE() != null) {
            return FileType.getType();
        } else if (ctx.FLOAT() != null) {
            return FloatType.getType();
        } else if (ctx.INT() != null) {
            return IntType.getType();
        } else if (ctx.Identifier() != null) {
            return StructType.getType(ctx.Identifier().getText());
        } else if (ctx.OBJECT() != null) {
            return ObjectType.getType();
        } else if (ctx.map_type() != null) {
            return visitMap_type(ctx.map_type());
        } else if (ctx.array_type() != null) {
            return visitArray_type(ctx.array_type());
        } else if (ctx.pair_type() != null) {
            return visitPair_type(ctx.pair_type());
        } else {
            return null;
        }
    }

    @Override
    public MapType visitMap_type(WdlV1Parser.Map_typeContext ctx) {
        Type left = visitWdl_type(ctx.wdl_type(0));
        Type right = visitWdl_type(ctx.wdl_type(1));
        return MapType.getType(left, right);
    }

    @Override
    public ArrayType visitArray_type(WdlV1Parser.Array_typeContext ctx) {
        Type innerType = visitWdl_type(ctx.wdl_type());
        boolean nonEmpty = ctx.PLUS() != null && ctx.PLUS().getText().equals("+");
        return ArrayType.getType(innerType, nonEmpty);

    }

    @Override
    public PairType visitPair_type(WdlV1Parser.Pair_typeContext ctx) {
        Type left = visitWdl_type(ctx.wdl_type(0));
        Type right = visitWdl_type(ctx.wdl_type(1));
        return PairType.getType(left, right);
    }

    @Override
    public Type visitWdl_type(WdlV1Parser.Wdl_typeContext ctx) {
        Type type = visitType_base(ctx.type_base());
        if (ctx.OPTIONAL() != null) {
            return OptionalType.getType(type);
        }
        return type;
    }

    @Override
    public Declaration visitUnbound_decls(WdlV1Parser.Unbound_declsContext ctx) {
        int id = getNextId();
        Type type = visitWdl_type(ctx.wdl_type());
        String name = ctx.Identifier().getText();
        return new Declaration(type, name, null, id);
    }

    @Override
    public Declaration visitBound_decls(WdlV1Parser.Bound_declsContext ctx) {
        int id = getNextId();
        Type type = visitWdl_type(ctx.wdl_type());
        Expression expression = visitExpr(ctx.expr());
        String name = ctx.Identifier().getText();
        return new Declaration(type, name, expression, id);
    }

    @Override
    public Declaration visitAny_decls(WdlV1Parser.Any_declsContext ctx) {
        return (Declaration) super.visitAny_decls(ctx);
    }

    @Override
    public Expression visitNumber(WdlV1Parser.NumberContext ctx) {
        int id = getNextId();
        if (ctx.IntLiteral() != null) {
            return new IntLiteral(Integer.parseInt(ctx.IntLiteral().getText()), id);
        } else {
            return new FloatLiteral(Float.parseFloat(ctx.FloatLiteral().getText()), id);
        }
    }

    @Override
    public Expression visitExpression_placeholder_option(WdlV1Parser.Expression_placeholder_optionContext ctx) {
        int id = getNextId();
        Expression expression = null;

        if (ctx.number() != null) {
            expression = visitNumber(ctx.number());
        } else if (ctx.string() != null) {
            expression = visitString(ctx.string());
        }

        if (ctx.DEFAULT() != null) {
            return new DefaultPlaceholder(expression, id);
        } else if (ctx.SEP() != null) {
            return new SepPlaceholder(expression, id);
        } else {
            if (Boolean.parseBoolean(ctx.BoolLiteral().getText())) {
                return new TrueFalsePlaceholder(expression, TrueFalsePlaceholder.Condition.TRUE, id);
            } else {
                return new TrueFalsePlaceholder(expression, TrueFalsePlaceholder.Condition.FALSE, id);
            }
        }
    }

    @Override
    public StringLiteral.StringPart visitString_expr_part(WdlV1Parser.String_expr_partContext ctx) {
        int id = getNextId();
        Expression expression = visitExpr(ctx.expr());
        List<Expression> placeholders = new ArrayList<>();
        if (ctx.expression_placeholder_option() != null) {
            for (WdlV1Parser.Expression_placeholder_optionContext placeholder : ctx.expression_placeholder_option()) {
                placeholders.add(visitExpression_placeholder_option(placeholder));
            }
        }
        return new StringLiteral.StringPart(placeholders, expression, id);
    }

    @Override
    public StringLiteral visitString(WdlV1Parser.StringContext ctx) {
        int id = getNextId();
        List<StringLiteral.StringPart> stringParts = new ArrayList<>();
        if (ctx.string_part() != null) {
            stringParts.add(new StringLiteral.StringPart(ctx.string_part().getText(), getNextId()));
        }
        if (ctx.string_expr_with_string_part() != null) {
            for (WdlV1Parser.String_expr_with_string_partContext part : ctx.string_expr_with_string_part()) {
                stringParts.add(visitString_expr_part(part.string_expr_part()));
                if (part.string_part() != null) {
                    stringParts.add(new StringLiteral.StringPart(part.string_part().getText(), getNextId()));
                }
            }
        }
        return new StringLiteral(stringParts, id);
    }

    @Override
    public Expression visitPrimitive_literal(WdlV1Parser.Primitive_literalContext ctx) {
        if (ctx.BoolLiteral() != null) {
            return new BooleanLiteral(Boolean.parseBoolean(ctx.BoolLiteral().getText()), getNextId());
        } else if (ctx.number() != null) {
            return visitNumber(ctx.number());
        } else if (ctx.Identifier() != null) {
            return new VariableReference(ctx.Identifier().getText(), getNextId());
        } else {
            return visitString(ctx.string());
        }
    }

    @Override
    public Expression visitExpr(WdlV1Parser.ExprContext ctx) {
        if (ctx.start.getLine() == 76) {
            return (Expression) super.visitExpr(ctx);
        }
        try {
            return (Expression) super.visitExpr(ctx);
        } catch (Exception e) {
            System.out.println(ctx.stop);
            throw new RuntimeException(e);
        }
    }

    @Override
    public BinaryExpression visitLor(WdlV1Parser.LorContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix0(ctx.expr_infix0()),
                                    visitExpr_infix1(ctx.expr_infix1()),
                                    BinaryExpression.BinaryOperation.LOGICAL_OR,
                                    id);
    }

    @Override
    public BinaryExpression visitLand(WdlV1Parser.LandContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix1(ctx.expr_infix1()),
                                    visitExpr_infix2(ctx.expr_infix2()),
                                    BinaryExpression.BinaryOperation.LOGICAL_AND,
                                    id);
    }

    @Override
    public BinaryExpression visitEqeq(WdlV1Parser.EqeqContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.EQUAL_TO,
                                    id);
    }

    @Override
    public BinaryExpression visitLt(WdlV1Parser.LtContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.LESS_THAN,
                                    id);
    }

    @Override
    public BinaryExpression visitGte(WdlV1Parser.GteContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.GREATER_THAN_OR_EQUAL,
                                    id);
    }

    @Override
    public BinaryExpression visitNeq(WdlV1Parser.NeqContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.NOT_EQUAL_TO,
                                    id);
    }

    @Override
    public BinaryExpression visitLte(WdlV1Parser.LteContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.LESS_THAN_OR_EQUAL,
                                    id);
    }

    @Override
    public BinaryExpression visitGt(WdlV1Parser.GtContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.GREATER_THAN,
                                    id);
    }

    @Override
    public BinaryExpression visitAdd(WdlV1Parser.AddContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix3(ctx.expr_infix3()),
                                    visitExpr_infix4(ctx.expr_infix4()),
                                    BinaryExpression.BinaryOperation.ADD,
                                    id);
    }

    @Override
    public BinaryExpression visitSub(WdlV1Parser.SubContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix3(ctx.expr_infix3()),
                                    visitExpr_infix4(ctx.expr_infix4()),
                                    BinaryExpression.BinaryOperation.SUBTRACT,
                                    id);
    }

    @Override
    public BinaryExpression visitMod(WdlV1Parser.ModContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix4(ctx.expr_infix4()),
                                    (Expression) visitExpr_infix5(ctx.expr_infix5()),
                                    BinaryExpression.BinaryOperation.MOD,
                                    id);
    }

    @Override
    public BinaryExpression visitMul(WdlV1Parser.MulContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix4(ctx.expr_infix4()),
                                    (Expression) visitExpr_infix5(ctx.expr_infix5()),
                                    BinaryExpression.BinaryOperation.MULTIPLY,
                                    id);
    }

    @Override
    public BinaryExpression visitDivide(WdlV1Parser.DivideContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix4(ctx.expr_infix4()),
                                    (Expression) visitExpr_infix5(ctx.expr_infix5()),
                                    BinaryExpression.BinaryOperation.DIVIDE,
                                    id);
    }

    @Override
    public PairLiteral visitPair_literal(WdlV1Parser.Pair_literalContext ctx) {
        int id = getNextId();
        Expression left = visitExpr(ctx.expr(0));
        Expression right = visitExpr(ctx.expr(1));
        return new PairLiteral(left, right, id);
    }

    @Override
    public ApplyFunction visitApply(WdlV1Parser.ApplyContext ctx) {
        int id = getNextId();
        String name = ctx.Identifier().getText();
        List<Expression> arguments = new ArrayList<>();
        if (ctx.expr() != null) {
            for (WdlV1Parser.ExprContext exprContext : ctx.expr()) {
                arguments.add(visitExpr(exprContext));
            }
        }
        return new ApplyFunction(name, arguments, id);
    }

    @Override
    public Expression visitExpression_group(WdlV1Parser.Expression_groupContext ctx) {
        return visitExpr(ctx.expr());
    }

    @Override
    public Expression visitPrimitives(WdlV1Parser.PrimitivesContext ctx) {
        return visitPrimitive_literal(ctx.primitive_literal());
    }

    @Override
    public VariableReference visitLeft_name(WdlV1Parser.Left_nameContext ctx) {
        int id = getNextId();
        return new VariableReference(ctx.Identifier().getText(), id);
    }

    @Override
    public IndexedAccessor visitAt(WdlV1Parser.AtContext ctx) {
        int id = getNextId();
        return new IndexedAccessor(visitExpressionCore(ctx.expr_core()), visitExpr(ctx.expr()), id);
    }

    @Override
    public Negate visitNegate(WdlV1Parser.NegateContext ctx) {

        int id = getNextId();
        return new Negate(visitExpr(ctx.expr()), id);
    }

    @Override
    public MapLiteral visitMap_literal(WdlV1Parser.Map_literalContext ctx) {
        int id = getNextId();
        List<MapLiteral.MapEntry> entries = new ArrayList<>();
        if (ctx.expr() != null) {
            int size = ctx.expr().size();
            for (int i = 0; i < size; i += 2) {
                Expression key = visitExpr(ctx.expr(i));
                Expression value = visitExpr(ctx.expr(i + 1));
                entries.add(new MapLiteral.MapEntry(key, value));
            }
        }

        return new MapLiteral(entries, id);
    }

    @Override
    public IfThenElse visitIfthenelse(WdlV1Parser.IfthenelseContext ctx) {
        int id = getNextId();
        Expression condition = visitExpr(ctx.expr(0));
        Expression ifTrue = visitExpr(ctx.expr(1));
        Expression ifFalse = visitExpr(ctx.expr(2));
        return new IfThenElse(condition, ifTrue, ifFalse, id);

    }

    @Override
    public DotAccessor visitGet_name(WdlV1Parser.Get_nameContext ctx) {
        int id = getNextId();
        return new DotAccessor(visitExpressionCore(ctx.expr_core()), ctx.Identifier().getText(), id);
    }

    @Override
    public ObjectLiteral visitObject_literal(WdlV1Parser.Object_literalContext ctx) {
        int id = getNextId();
        Map<String, Expression> values = new HashMap<>();
        if (!ctx.isEmpty()) {
            for (int i = 0; i < ctx.Identifier().size(); i++) {
                values.put(ctx.Identifier(i).getText(), visitExpr(ctx.expr(i)));
            }
        }
        return new ObjectLiteral(values, id);
    }

    @Override
    public ArrayLiteral visitArray_literal(WdlV1Parser.Array_literalContext ctx) {
        int id = getNextId();
        List<Expression> values = new ArrayList<>();
        if (!ctx.isEmpty()) {
            for (int i = 0; i < ctx.expr().size(); i++) {
                values.add(visitExpr(ctx.expr(i)));
            }
        }
        return new ArrayLiteral(values, id);
    }

    @Override
    public Version visitVersion(WdlV1Parser.VersionContext ctx) {
        int id = getNextId();
        return new Version(ctx.ReleaseVersion().getText(), id);
    }

    @Override
    public Import.ImportAlias visitImport_alias(WdlV1Parser.Import_aliasContext ctx) {
        int id = getNextId();
        String name = ctx.Identifier(0).getText();
        String alias = ctx.Identifier(1).getText();
        return new Import.ImportAlias(name, alias, id);
    }

    @Override
    public Import visitImport_doc(WdlV1Parser.Import_docContext ctx) {
        int id = getNextId();
        String importUrl = ctx.string().string_part().getText();
        String name = null;
        if (ctx.import_as() != null) {
            name = ctx.import_as().Identifier().getText();
        }
        List<Import.ImportAlias> aliases = new ArrayList<>();
        if (ctx.import_alias() != null) {
            for (int i = 0; i < ctx.import_alias().size(); i++) {
                aliases.add(visitImport_alias(ctx.import_alias(i)));
            }
        }
        return Import.newBuilder().url(importUrl).id(id).name(name).aliases(aliases).build();
    }

    @Override
    public Struct visitStruct(WdlV1Parser.StructContext ctx) {
        int id = getNextId();
        String name = ctx.Identifier().getText();
        List<Declaration> members = new ArrayList<>();
        if (ctx.unbound_decls() != null) {
            for (int i = 0; i < ctx.unbound_decls().size(); i++) {
                members.add(visitUnbound_decls(ctx.unbound_decls(i)));
            }
        }
        return Struct.newBuilder().name(name).members(members).id(id).build();

    }

    @Override
    public ParameterMeta visitParameter_meta(WdlV1Parser.Parameter_metaContext ctx) {
        int id = getNextId();
        Map<String, Object> parameterMetaAttributes = new HashMap<>();
        if (ctx.meta_kv() != null && !ctx.meta_kv().isEmpty()) {
            for (WdlV1Parser.Meta_kvContext metaKv : ctx.meta_kv()) {
                String key = metaKv.MetaIdentifier().getText();
                Object value = visitMetaValue(metaKv.meta_value());
                parameterMetaAttributes.put(key, value);
            }
        }
        return ParameterMeta.newBuilder().attributes(parameterMetaAttributes).id(id).build();
    }

    @Override
    public Meta visitMeta(WdlV1Parser.MetaContext ctx) {
        int id = getNextId();
        Map<String, Object> metaAttributes = new HashMap<>();
        if (ctx.meta_kv() != null && !ctx.meta_kv().isEmpty()) {
            for (WdlV1Parser.Meta_kvContext metaKv : ctx.meta_kv()) {
                String key = metaKv.MetaIdentifier().getText();
                Object value = visitMetaValue(metaKv.meta_value());
                metaAttributes.put(key, value);
            }
        }
        return Meta.newBuilder().attributes(metaAttributes).id(id).build();
    }

    @Override
    public Runtime visitTask_runtime(WdlV1Parser.Task_runtimeContext ctx) {
        int id = getNextId();
        Map<String, Expression> attributes = new HashMap<>();
        if (ctx.task_runtime_kv() != null) {
            for (int i = 0; i < ctx.task_runtime_kv().size(); i++) {
                WdlV1Parser.Task_runtime_kvContext kvContext = ctx.task_runtime_kv(i);
                String key = kvContext.Identifier().getText();
                Expression value = visitExpr(kvContext.expr());
                attributes.put(key, value);
            }
        }
        return Runtime.newBuilder().attributes(attributes).id(id).build();
    }

    @Override
    public Inputs visitTask_input(WdlV1Parser.Task_inputContext ctx) {
        int id = getNextId();
        List<Declaration> declarations = new ArrayList<>();
        if (ctx.any_decls() != null) {
            for (int i = 0; i < ctx.any_decls().size(); i++) {
                declarations.add(visitAny_decls(ctx.any_decls(i)));
            }
        }
        return Inputs.newBuilder().declarations(declarations).id(id).build();
    }

    @Override
    public Outputs visitTask_output(WdlV1Parser.Task_outputContext ctx) {
        int id = getNextId();
        List<Declaration> declarations = new ArrayList<>();
        if (ctx.bound_decls() != null) {
            for (int i = 0; i < ctx.bound_decls().size(); i++) {
                declarations.add(visitBound_decls(ctx.bound_decls(i)));
            }
        }
        return Outputs.newBuilder().declarations(declarations).id(id).build();
    }

    @Override
    public Command.CommandPart visitTask_command_expr_part(WdlV1Parser.Task_command_expr_partContext ctx) {
        int id = getNextId();
        Expression expression = visitExpr(ctx.expr());
        List<Expression> placeholders = new ArrayList<>();
        if (ctx.expression_placeholder_option() != null) {
            for (WdlV1Parser.Expression_placeholder_optionContext placeholder : ctx.expression_placeholder_option()) {
                placeholders.add(visitExpression_placeholder_option(placeholder));
            }
        }
        return new Command.CommandPart(placeholders, expression, id);
    }

    @Override
    public Command visitTask_command(WdlV1Parser.Task_commandContext ctx) {
        int id = getNextId();
        List<Command.CommandPart> commandParts = new ArrayList<>();
        if (ctx.task_command_string_part() != null) {
            commandParts.add(new Command.CommandPart(ctx.task_command_string_part().getText(), id));
        }
        if (ctx.task_command_expr_with_string() != null) {
            for (WdlV1Parser.Task_command_expr_with_stringContext part : ctx.task_command_expr_with_string()) {
                commandParts.add(visitTask_command_expr_part(part.task_command_expr_part()));
                if (part.task_command_string_part() != null) {
                    commandParts.add(new Command.CommandPart(part.task_command_string_part().getText(), getNextId()));
                }
            }
        }
        return Command.newBuilder().commandParts(commandParts).id(id).build();
    }

    @Override
    public WdlElement visitTask_element(WdlV1Parser.Task_elementContext ctx) {
        return super.visitTask_element(ctx);
    }

    @Override
    public Task visitTask(WdlV1Parser.TaskContext ctx) {
        int id = getNextId();
        String name = ctx.Identifier().getText();
        List<Declaration> declarations = new ArrayList<>();
        Inputs inputs = null;
        Outputs outputs = null;
        Runtime runtime = null;
        ParameterMeta parameterMeta = null;
        Meta meta = null;
        Command command = null;

        List<WdlV1Parser.Task_elementContext> elements = ctx.task_element();
        if (elements != null) {
            for (int i = 0; i < elements.size(); i++) {
                WdlV1Parser.Task_elementContext element = elements.get(i);
                if (element.bound_decls() != null) {
                    declarations.add(visitBound_decls(element.bound_decls()));
                } else if (element.task_input() != null) {
                    inputs = visitTask_input(element.task_input());
                } else if (element.task_output() != null) {
                    outputs = visitTask_output(element.task_output());
                } else if (element.task_command() != null) {
                    command = visitTask_command(element.task_command());
                } else if (element.task_runtime() != null) {
                    runtime = visitTask_runtime(element.task_runtime());
                } else if (element.parameter_meta() != null) {
                    parameterMeta = visitParameter_meta(element.parameter_meta());
                } else if (element.meta() != null) {
                    meta = visitMeta(element.meta());
                }
            }
        }
        return Task.newBuilder()
                   .name(name)
                   .inputs(inputs)
                   .declarations(declarations)
                   .command(command)
                   .runtime(runtime)
                   .outputs(outputs)
                   .meta(meta)
                   .parameterMeta(parameterMeta)
                   .id(id)
                   .build();
    }

    @Override
    public Call visitCall(WdlV1Parser.CallContext ctx) {
        int id = getNextId();
        String name = ctx.call_name().getText();
        String alias = ctx.call_alias() != null ? ctx.call_alias().Identifier().getText() : null;
        Map<String, Expression> inputs = new HashMap<>();
        if (ctx.call_body() != null && ctx.call_body().call_inputs() != null) {
            WdlV1Parser.Call_inputsContext callInputs = ctx.call_body().call_inputs();
            if (callInputs.call_input() != null) {
                for (int i = 0; i < callInputs.call_input().size(); i++) {
                    WdlV1Parser.Call_inputContext callInput = callInputs.call_input(i);
                    String inputName = callInput.Identifier().getText();
                    Expression inputValue = visitExpr(callInput.expr());
                    inputs.put(inputName, inputValue);
                }
            }
        }
        return Call.newBuilder().taskOrWorkflowName(name).callAlias(alias).inputs(inputs).id(id).build();
    }

    @Override
    public Scatter visitScatter(WdlV1Parser.ScatterContext ctx) {
        int id = getNextId();
        Scatter scatter = new Scatter();
        scatter.setId(id);
        scatter.setVarname(ctx.Identifier().getText());
        scatter.setExpression(visitExpr(ctx.expr()));
        List<WdlElement> workflowElements = new ArrayList<>();
        if (ctx.inner_workflow_element() != null) {
            for (int i = 0; i < ctx.inner_workflow_element().size(); i++) {
                WdlElement element = visitChildren(ctx.inner_workflow_element(i));
                workflowElements.add(element);
            }
        }

        scatter.setWorkflowElements(workflowElements);
        return scatter;
    }

    @Override
    public Conditional visitConditional(WdlV1Parser.ConditionalContext ctx) {
        int id = getNextId();
        Conditional conditional = new Conditional();
        conditional.setExpression(visitExpr(ctx.expr()));
        conditional.setId(id);
        List<WdlElement> workflowElements = new ArrayList<>();
        if (ctx.inner_workflow_element() != null) {
            for (int i = 0; i < ctx.inner_workflow_element().size(); i++) {
                WdlElement element = visitChildren(ctx.inner_workflow_element(i));
                workflowElements.add(element);
            }
        }

        conditional.setElements(workflowElements);
        return conditional;
    }

    @Override
    public Inputs visitWorkflow_input(WdlV1Parser.Workflow_inputContext ctx) {
        int id = getNextId();
        List<Declaration> declarations = new ArrayList<>();
        if (ctx.any_decls() != null) {
            for (int i = 0; i < ctx.any_decls().size(); i++) {
                declarations.add(visitAny_decls(ctx.any_decls(i)));
            }
        }
        return Inputs.newBuilder().declarations(declarations).id(id).build();
    }

    @Override
    public Outputs visitWorkflow_output(WdlV1Parser.Workflow_outputContext ctx) {
        int id = getNextId();
        List<Declaration> declarations = new ArrayList<>();
        if (ctx.bound_decls() != null) {
            for (int i = 0; i < ctx.bound_decls().size(); i++) {
                declarations.add(visitBound_decls(ctx.bound_decls(i)));
            }
        }
        return Outputs.newBuilder().declarations(declarations).id(id).build();
    }

    @Override
    public Inputs visitInput(WdlV1Parser.InputContext ctx) {
        return visitWorkflow_input(ctx.workflow_input());
    }

    @Override
    public Outputs visitOutput(WdlV1Parser.OutputContext ctx) {
        return visitWorkflow_output(ctx.workflow_output());
    }

    @Override
    public Meta visitMeta_element(WdlV1Parser.Meta_elementContext ctx) {
        return visitMeta(ctx.meta());
    }

    @Override
    public ParameterMeta visitParameter_meta_element(WdlV1Parser.Parameter_meta_elementContext ctx) {
        return visitParameter_meta(ctx.parameter_meta());
    }

    @Override
    public Workflow visitWorkflow(WdlV1Parser.WorkflowContext ctx) {
        int id = getNextId();
        Workflow workflow = new Workflow();
        workflow.setName(ctx.Identifier().getText());
        Inputs inputs = null;
        Outputs outputs = null;
        ParameterMeta parameterMeta = null;
        Meta meta = null;
        List<WdlElement> elements = new ArrayList<>();

        for (WdlV1Parser.Workflow_elementContext elementContext : ctx.workflow_element()) {
            WdlElement element = visitChildren(elementContext);
            if (element instanceof Inputs) {
                inputs = (Inputs) element;
            } else if (element instanceof Outputs) {
                outputs = (Outputs) element;
            } else if (element instanceof Meta) {
                meta = (Meta) element;
            } else if (element instanceof ParameterMeta) {
                parameterMeta = (ParameterMeta) element;
            } else if (element instanceof WdlElement) {
                elements.add(element);
            }
        }

        workflow.setInputs(inputs);
        workflow.setOutputs(outputs);
        workflow.setElements(elements);
        workflow.setMeta(meta);
        workflow.setParameterMeta(parameterMeta);
        workflow.setId(id);
        return workflow;
    }

    @Override
    public WdlElement visitDocument_element(WdlV1Parser.Document_elementContext ctx) {
        return super.visitDocument_element(ctx);
    }

    @Override
    public Document visitDocument(WdlV1Parser.DocumentContext ctx) {
        int id = getNextId();
        Document document = new Document();
        document.setVersion(visitVersion(ctx.version()));

        List<Import> imports = new ArrayList<>();
        List<Struct> structs = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        Workflow workflow = ctx.workflow() != null ? visitWorkflow(ctx.workflow()) : null;

        document.setWorkflow(workflow);

        for (WdlV1Parser.Document_elementContext elementContext : ctx.document_element()) {
            WdlElement element = visitChildren(elementContext);
            if (element instanceof Import) {
                imports.add((Import) element);
            } else if (element instanceof Struct) {
                structs.add((Struct) element);
            } else if (element instanceof Task) {
                tasks.add((Task) element);
            }
        }

        document.setImports(imports);
        document.setStructs(structs);
        document.setTasks(tasks);
        document.setId(id);
        document.setLib(new WdlV1StandardLib());
        document.setLanguageLevel(LanguageLevel.WDL_V1);
        document.setCoercionOptions(new CoercionOptions(LanguageLevel.WDL_V1));
        return document;
    }
}
