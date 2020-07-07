package com.dnastack.wdl4j;

import com.dnastack.wdl4j.lib.Runtime;
import com.dnastack.wdl4j.lib.*;
import com.dnastack.wdl4j.lib.api.WdlElement;
import com.dnastack.wdl4j.lib.expression.*;
import com.dnastack.wdl4j.lib.expression.literal.*;
import com.dnastack.wdl4j.lib.stdlib.WdlDraft2StandardLib;
import com.dnastack.wdl4j.lib.typing.*;
import org.openwdl.wdl.parser.WdlDraft2Parser;
import org.openwdl.wdl.parser.WdlDraft2ParserBaseVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class WdlDraft2DocumentVisitor extends WdlDraft2ParserBaseVisitor<WdlElement> {

    private AtomicInteger idCounter = new AtomicInteger(0);

    private int getNextId() {
        return idCounter.addAndGet(1);
    }

    private Expression visitExpr_infix0(WdlDraft2Parser.Expr_infix0Context ctx) {
        if (ctx instanceof WdlDraft2Parser.LorContext) {
            return visitLor((WdlDraft2Parser.LorContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Infix1Context) {
            return (Expression) visitInfix1((WdlDraft2Parser.Infix1Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix1(WdlDraft2Parser.Expr_infix1Context ctx) {
        if (ctx instanceof WdlDraft2Parser.LandContext) {
            return visitLand((WdlDraft2Parser.LandContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Infix2Context) {
            return (Expression) visitInfix2((WdlDraft2Parser.Infix2Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix2(WdlDraft2Parser.Expr_infix2Context ctx) {
        if (ctx instanceof WdlDraft2Parser.EqeqContext) {
            return visitEqeq((WdlDraft2Parser.EqeqContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.GteContext) {
            return visitGte((WdlDraft2Parser.GteContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.GtContext) {
            return visitGt((WdlDraft2Parser.GtContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.LtContext) {
            return visitLt((WdlDraft2Parser.LtContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.LteContext) {
            return visitLte((WdlDraft2Parser.LteContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Infix3Context) {
            return (Expression) visitInfix3((WdlDraft2Parser.Infix3Context) ctx);
        } else if (ctx instanceof WdlDraft2Parser.NeqContext) {
            return visitNeq((WdlDraft2Parser.NeqContext) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix3(WdlDraft2Parser.Expr_infix3Context ctx) {
        if (ctx instanceof WdlDraft2Parser.AddContext) {
            return visitAdd((WdlDraft2Parser.AddContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.SubContext) {
            return visitSub((WdlDraft2Parser.SubContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Infix4Context) {
            return (Expression) visitInfix4((WdlDraft2Parser.Infix4Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpr_infix4(WdlDraft2Parser.Expr_infix4Context ctx) {
        if (ctx instanceof WdlDraft2Parser.ModContext) {
            return visitMod((WdlDraft2Parser.ModContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.MulContext) {
            return visitMul((WdlDraft2Parser.MulContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.DivideContext) {
            return visitDivide((WdlDraft2Parser.DivideContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Infix5Context) {
            return (Expression) visitInfix5((WdlDraft2Parser.Infix5Context) ctx);
        } else {
            return (Expression) visitChildren(ctx);
        }
    }

    private Expression visitExpressionCore(WdlDraft2Parser.Expr_coreContext ctx) {
        if (ctx instanceof WdlDraft2Parser.ApplyContext) {
            return visitApply((WdlDraft2Parser.ApplyContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Array_literalContext) {
            return visitArray_literal((WdlDraft2Parser.Array_literalContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Pair_literalContext) {
            return visitPair_literal((WdlDraft2Parser.Pair_literalContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Map_literalContext) {
            return visitMap_literal((WdlDraft2Parser.Map_literalContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Object_literalContext) {
            return visitObject_literal((WdlDraft2Parser.Object_literalContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.IfthenelseContext) {
            return visitIfthenelse((WdlDraft2Parser.IfthenelseContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Expression_groupContext) {
            return visitExpression_group((WdlDraft2Parser.Expression_groupContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.AtContext) {
            return visitAt((WdlDraft2Parser.AtContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Get_nameContext) {
            return visitGet_name((WdlDraft2Parser.Get_nameContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.NegateContext) {
            return visitNegate((WdlDraft2Parser.NegateContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.UnarysignedContext) {
            return (Expression) visitUnarysigned((WdlDraft2Parser.UnarysignedContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.PrimitivesContext) {
            return visitPrimitives((WdlDraft2Parser.PrimitivesContext) ctx);
        } else if (ctx instanceof WdlDraft2Parser.Left_nameContext) {
            return visitLeft_name((WdlDraft2Parser.Left_nameContext) ctx);
        } else {
            return null;
        }
    }

    @Override
    public Type visitType_base(WdlDraft2Parser.Type_baseContext ctx) {
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
    public MapType visitMap_type(WdlDraft2Parser.Map_typeContext ctx) {
        Type left = visitWdl_type(ctx.wdl_type(0));
        Type right = visitWdl_type(ctx.wdl_type(1));
        return MapType.getType(left, right);
    }

    @Override
    public ArrayType visitArray_type(WdlDraft2Parser.Array_typeContext ctx) {
        Type innerType = visitWdl_type(ctx.wdl_type());
        boolean nonEmpty = ctx.PLUS() != null && ctx.PLUS().getText().equals("+");
        return ArrayType.getType(innerType, nonEmpty);

    }

    @Override
    public PairType visitPair_type(WdlDraft2Parser.Pair_typeContext ctx) {
        Type left = visitWdl_type(ctx.wdl_type(0));
        Type right = visitWdl_type(ctx.wdl_type(1));
        return PairType.getType(left, right);
    }

    @Override
    public Type visitWdl_type(WdlDraft2Parser.Wdl_typeContext ctx) {
        Type type = visitType_base(ctx.type_base());
        if (ctx.OPTIONAL() != null) {
            return OptionalType.getType(type);
        }
        return type;
    }

    @Override
    public Declaration visitUnbound_decls(WdlDraft2Parser.Unbound_declsContext ctx) {
        int id = getNextId();
        Type type = visitWdl_type(ctx.wdl_type());
        String name = ctx.Identifier().getText();
        return new Declaration(type, name, null, id);
    }

    @Override
    public Declaration visitBound_decls(WdlDraft2Parser.Bound_declsContext ctx) {
        int id = getNextId();
        Type type = visitWdl_type(ctx.wdl_type());
        Expression expression = visitExpr(ctx.expr());
        String name = ctx.Identifier().getText();
        return new Declaration(type, name, expression, id);
    }

    @Override
    public Declaration visitAny_decls(WdlDraft2Parser.Any_declsContext ctx) {
        return (Declaration) super.visitAny_decls(ctx);
    }

    @Override
    public Expression visitNumber(WdlDraft2Parser.NumberContext ctx) {
        int id = getNextId();
        if (ctx.IntLiteral() != null) {
            return new IntLiteral(Integer.parseInt(ctx.IntLiteral().getText()), id);
        } else {
            return new FloatLiteral(Float.parseFloat(ctx.FloatLiteral().getText()), id);
        }
    }

    @Override
    public Expression visitExpression_placeholder_option(WdlDraft2Parser.Expression_placeholder_optionContext ctx) {
        int id = getNextId();
        Expression expression = null;

        if (ctx.number() != null) {
            expression = visitNumber(ctx.number());
        } else if (ctx.string() != null) {
            expression = visitString(ctx.string());
        } else if (ctx.Identifier() != null){
            expression = new VariableReference(ctx.Identifier().getText(),getNextId());
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
    public StringLiteral.StringPart visitString_expr_part(WdlDraft2Parser.String_expr_partContext ctx) {
        int id = getNextId();
        Expression expression = visitExpr(ctx.expr());
        List<Expression> placeholders = new ArrayList<>();
        if (ctx.expression_placeholder_option() != null) {
            for (WdlDraft2Parser.Expression_placeholder_optionContext placeholder : ctx.expression_placeholder_option()) {
                placeholders.add(visitExpression_placeholder_option(placeholder));
            }
        }
        return new StringLiteral.StringPart(placeholders, expression, id);
    }

    @Override
    public StringLiteral visitString(WdlDraft2Parser.StringContext ctx) {
        int id = getNextId();
        List<StringLiteral.StringPart> stringParts = new ArrayList<>();
        if (ctx.string_part() != null) {
            stringParts.add(new StringLiteral.StringPart(ctx.string_part().getText(), getNextId()));
        }
        if (ctx.string_expr_with_string_part() != null) {
            for (WdlDraft2Parser.String_expr_with_string_partContext part : ctx.string_expr_with_string_part()) {
                stringParts.add(visitString_expr_part(part.string_expr_part()));
                if (part.string_part() != null) {
                    stringParts.add(new StringLiteral.StringPart(part.string_part().getText(), getNextId()));
                }
            }
        }
        return new StringLiteral(stringParts, id);
    }

    @Override
    public Expression visitPrimitive_literal(WdlDraft2Parser.Primitive_literalContext ctx) {
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
    public Expression visitExpr(WdlDraft2Parser.ExprContext ctx) {
        return (Expression) super.visitExpr(ctx);
    }

    @Override
    public BinaryExpression visitLor(WdlDraft2Parser.LorContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix0(ctx.expr_infix0()),
                                    visitExpr_infix1(ctx.expr_infix1()),
                                    BinaryExpression.BinaryOperation.LOGICAL_OR,
                                    id);
    }

    @Override
    public BinaryExpression visitLand(WdlDraft2Parser.LandContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix1(ctx.expr_infix1()),
                                    visitExpr_infix2(ctx.expr_infix2()),
                                    BinaryExpression.BinaryOperation.LOGICAL_AND,
                                    id);
    }

    @Override
    public BinaryExpression visitEqeq(WdlDraft2Parser.EqeqContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.EQUAL_TO,
                                    id);
    }

    @Override
    public BinaryExpression visitLt(WdlDraft2Parser.LtContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.LESS_THAN,
                                    id);
    }

    @Override
    public BinaryExpression visitGte(WdlDraft2Parser.GteContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.GREATER_THAN_OR_EQUAL,
                                    id);
    }

    @Override
    public BinaryExpression visitNeq(WdlDraft2Parser.NeqContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.NOT_EQUAL_TO,
                                    id);
    }

    @Override
    public BinaryExpression visitLte(WdlDraft2Parser.LteContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.LESS_THAN_OR_EQUAL,
                                    id);
    }

    @Override
    public BinaryExpression visitGt(WdlDraft2Parser.GtContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix2(ctx.expr_infix2()),
                                    visitExpr_infix3(ctx.expr_infix3()),
                                    BinaryExpression.BinaryOperation.GREATER_THAN,
                                    id);
    }

    @Override
    public BinaryExpression visitAdd(WdlDraft2Parser.AddContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix3(ctx.expr_infix3()),
                                    visitExpr_infix4(ctx.expr_infix4()),
                                    BinaryExpression.BinaryOperation.ADD,
                                    id);
    }

    @Override
    public BinaryExpression visitSub(WdlDraft2Parser.SubContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix3(ctx.expr_infix3()),
                                    visitExpr_infix4(ctx.expr_infix4()),
                                    BinaryExpression.BinaryOperation.SUBTRACT,
                                    id);
    }

    @Override
    public BinaryExpression visitMod(WdlDraft2Parser.ModContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix4(ctx.expr_infix4()),
                                    (Expression) visitExpr_infix5(ctx.expr_infix5()),
                                    BinaryExpression.BinaryOperation.MOD,
                                    id);
    }

    @Override
    public BinaryExpression visitMul(WdlDraft2Parser.MulContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix4(ctx.expr_infix4()),
                                    (Expression) visitExpr_infix5(ctx.expr_infix5()),
                                    BinaryExpression.BinaryOperation.MULTIPLY,
                                    id);
    }

    @Override
    public BinaryExpression visitDivide(WdlDraft2Parser.DivideContext ctx) {
        int id = getNextId();
        return new BinaryExpression(visitExpr_infix4(ctx.expr_infix4()),
                                    (Expression) visitExpr_infix5(ctx.expr_infix5()),
                                    BinaryExpression.BinaryOperation.DIVIDE,
                                    id);
    }

    @Override
    public PairLiteral visitPair_literal(WdlDraft2Parser.Pair_literalContext ctx) {
        int id = getNextId();
        Expression left = visitExpr(ctx.expr(0));
        Expression right = visitExpr(ctx.expr(1));
        return new PairLiteral(left, right, id);
    }

    @Override
    public ApplyFunction visitApply(WdlDraft2Parser.ApplyContext ctx) {
        int id = getNextId();
        String name = ctx.Identifier().getText();
        List<Expression> arguments = new ArrayList<>();
        if (ctx.expr() != null) {
            for (WdlDraft2Parser.ExprContext exprContext : ctx.expr()) {
                arguments.add(visitExpr(exprContext));
            }
        }
        return new ApplyFunction(name, arguments, id);
    }

    @Override
    public Expression visitExpression_group(WdlDraft2Parser.Expression_groupContext ctx) {
        return visitExpr(ctx.expr());
    }

    @Override
    public Expression visitPrimitives(WdlDraft2Parser.PrimitivesContext ctx) {
        return visitPrimitive_literal(ctx.primitive_literal());
    }

    @Override
    public VariableReference visitLeft_name(WdlDraft2Parser.Left_nameContext ctx) {
        int id = getNextId();
        return new VariableReference(ctx.Identifier().getText(), id);
    }

    @Override
    public IndexedAccessor visitAt(WdlDraft2Parser.AtContext ctx) {
        int id = getNextId();
        return new IndexedAccessor((Expression) visitChildren(ctx.expr_core()), visitExpr(ctx.expr()), id);
    }

    @Override
    public Negate visitNegate(WdlDraft2Parser.NegateContext ctx) {

        int id = getNextId();
        return new Negate(visitExpr(ctx.expr()), id);
    }

    @Override
    public MapLiteral visitMap_literal(WdlDraft2Parser.Map_literalContext ctx) {
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
    public IfThenElse visitIfthenelse(WdlDraft2Parser.IfthenelseContext ctx) {
        int id = getNextId();
        Expression condition = visitExpr(ctx.expr(0));
        Expression ifTrue = visitExpr(ctx.expr(1));
        Expression ifFalse = visitExpr(ctx.expr(2));
        return new IfThenElse(condition, ifTrue, ifFalse, id);

    }

    @Override
    public DotAccessor visitGet_name(WdlDraft2Parser.Get_nameContext ctx) {
        int id = getNextId();
        return new DotAccessor((Expression) visitChildren(ctx.expr_core()), ctx.Identifier().getText(), id);
    }

    @Override
    public ObjectLiteral visitObject_literal(WdlDraft2Parser.Object_literalContext ctx) {
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
    public ArrayLiteral visitArray_literal(WdlDraft2Parser.Array_literalContext ctx) {
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
    public Import visitImport_doc(WdlDraft2Parser.Import_docContext ctx) {
        int id = getNextId();
        String importUrl = ctx.string().string_part().getText();
        String name = null;
        if (ctx.import_as() != null) {
            name = ctx.import_as().Identifier().getText();
        }

        return Import.newBuilder().url(importUrl).id(id).name(name).build();
    }

    @Override
    public ParameterMeta visitParameter_meta(WdlDraft2Parser.Parameter_metaContext ctx) {
        int id = getNextId();
        Map<String, Object> parameterMetaAttributes = new HashMap<>();
        if (ctx.meta_kv() != null && !ctx.meta_kv().isEmpty()) {
            for (WdlDraft2Parser.Meta_kvContext metaKv : ctx.meta_kv()) {
                String key = metaKv.Identifier().getText();
                String value = metaKv.string().getText();
                parameterMetaAttributes.put(key, value);
            }
        }
        return ParameterMeta.newBuilder().attributes(parameterMetaAttributes).id(id).build();
    }

    @Override
    public Meta visitMeta(WdlDraft2Parser.MetaContext ctx) {
        int id = getNextId();
        Map<String, Object> metaAttributes = new HashMap<>();
        if (ctx.meta_kv() != null && !ctx.meta_kv().isEmpty()) {
            for (WdlDraft2Parser.Meta_kvContext metaKv : ctx.meta_kv()) {
                String key = metaKv.Identifier().getText();
                String value = metaKv.string().getText();
                metaAttributes.put(key, value);
            }
        }
        return Meta.newBuilder().attributes(metaAttributes).id(id).build();
    }

    @Override
    public com.dnastack.wdl4j.lib.Runtime visitTask_runtime(WdlDraft2Parser.Task_runtimeContext ctx) {
        int id = getNextId();
        Map<String, Expression> attributes = new HashMap<>();
        if (ctx.task_runtime_kv() != null) {
            for (int i = 0; i < ctx.task_runtime_kv().size(); i++) {
                WdlDraft2Parser.Task_runtime_kvContext kvContext = ctx.task_runtime_kv(i);
                String key = kvContext.Identifier().getText();
                Expression value = visitExpr(kvContext.expr());
                attributes.put(key, value);
            }
        }
        return com.dnastack.wdl4j.lib.Runtime.newBuilder().attributes(attributes).id(id).build();
    }

    @Override
    public Inputs visitTask_input(WdlDraft2Parser.Task_inputContext ctx) {
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
    public Outputs visitTask_output(WdlDraft2Parser.Task_outputContext ctx) {
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
    public Command.CommandPart visitTask_command_expr_part(WdlDraft2Parser.Task_command_expr_partContext ctx) {
        int id = getNextId();
        Expression expression = visitExpr(ctx.expr());
        List<Expression> placeholders = new ArrayList<>();
        if (ctx.expression_placeholder_option() != null) {
            for (WdlDraft2Parser.Expression_placeholder_optionContext placeholder : ctx.expression_placeholder_option()) {
                placeholders.add(visitExpression_placeholder_option(placeholder));
            }
        }
        return new Command.CommandPart(placeholders, expression, id);
    }

    @Override
    public Command visitTask_command(WdlDraft2Parser.Task_commandContext ctx) {
        int id = getNextId();
        List<Command.CommandPart> commandParts = new ArrayList<>();
        if (ctx.task_command_string_part() != null) {
            commandParts.add(new Command.CommandPart(ctx.task_command_string_part().getText(), id));
        }
        if (ctx.task_command_expr_with_string() != null) {
            for (WdlDraft2Parser.Task_command_expr_with_stringContext part : ctx.task_command_expr_with_string()) {
                commandParts.add(visitTask_command_expr_part(part.task_command_expr_part()));
                if (part.task_command_string_part() != null) {
                    commandParts.add(new Command.CommandPart(part.task_command_string_part().getText(), getNextId()));
                }
            }
        }
        return Command.newBuilder().commandParts(commandParts).id(id).build();
    }

    @Override
    public Task visitTask(WdlDraft2Parser.TaskContext ctx) {
        int id = getNextId();
        String name = ctx.Identifier().getText();
        Outputs outputs = null;
        Runtime runtime = null;
        ParameterMeta parameterMeta = null;
        Meta meta = null;
        Command command = null;
        Inputs inputs = visitTask_input(ctx.task_input());
        List<WdlDraft2Parser.Task_elementContext> elements = ctx.task_element();
        if (elements != null) {
            for (int i = 0; i < elements.size(); i++) {
                WdlDraft2Parser.Task_elementContext element = elements.get(i);
                if (element instanceof WdlDraft2Parser.Task_output_elementContext) {
                    outputs = visitTask_output(((WdlDraft2Parser.Task_output_elementContext) element).task_output());
                } else if (element instanceof WdlDraft2Parser.Task_command_elementContext) {
                    command = visitTask_command(((WdlDraft2Parser.Task_command_elementContext) element).task_command());
                } else if (element instanceof WdlDraft2Parser.Task_runtime_elementContext) {
                    runtime = visitTask_runtime(((WdlDraft2Parser.Task_runtime_elementContext) element).task_runtime());
                } else if (element instanceof WdlDraft2Parser.Task_parameter_meta_elementContext) {
                    parameterMeta = visitParameter_meta(((WdlDraft2Parser.Task_parameter_meta_elementContext) element).parameter_meta());
                } else if (element instanceof WdlDraft2Parser.Task_meta_elementContext) {
                    meta = visitMeta(((WdlDraft2Parser.Task_meta_elementContext) element).meta());
                }
            }
        }
        return Task.newBuilder()
                   .name(name)
                   .inputs(inputs)
                   .command(command)
                   .runtime(runtime)
                   .outputs(outputs)
                   .meta(meta)
                   .parameterMeta(parameterMeta)
                   .id(id)
                   .build();
    }

    @Override
    public Call visitCall(WdlDraft2Parser.CallContext ctx) {
        int id = getNextId();
        String name = ctx.call_name().getText();
        String alias = ctx.call_alias() != null ? ctx.call_alias().Identifier().getText() : null;
        Map<String, Expression> inputs = new HashMap<>();
        if (ctx.call_body() != null && ctx.call_body().call_inputs() != null) {
            WdlDraft2Parser.Call_inputsContext callInputs = ctx.call_body().call_inputs();
            if (callInputs.call_input() != null) {
                for (int i = 0; i < callInputs.call_input().size(); i++) {
                    WdlDraft2Parser.Call_inputContext callInput = callInputs.call_input(i);
                    String inputName = callInput.Identifier().getText();
                    Expression inputValue = visitExpr(callInput.expr());
                    inputs.put(inputName, inputValue);
                }
            }
        }
        return Call.newBuilder().taskOrWorkflowName(name).callAlias(alias).inputs(inputs).id(id).build();
    }

    @Override
    public Scatter visitScatter(WdlDraft2Parser.ScatterContext ctx) {
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
    public Conditional visitConditional(WdlDraft2Parser.ConditionalContext ctx) {
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
    public Outputs visitWorkflow_output(WdlDraft2Parser.Workflow_outputContext ctx) {
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
    public Workflow visitWorkflow(WdlDraft2Parser.WorkflowContext ctx) {
        int id = getNextId();
        Workflow workflow = new Workflow();
        workflow.setName(ctx.Identifier().getText());
        List<Declaration> inputDeclarations = new ArrayList<>();
        Outputs outputs = null;
        ParameterMeta parameterMeta = null;
        Meta meta = null;
        List<WdlElement> elements = new ArrayList<>();
        for (WdlDraft2Parser.Workflow_elementContext elementContext : ctx.workflow_element()) {
            // In Draft-2 Inputs are not explicitly declared within an input block. therefore we treat unbound decls as input values
            if (elementContext instanceof WdlDraft2Parser.Wf_decl_elementContext) {
                Declaration decl = visitUnbound_decls(((WdlDraft2Parser.Wf_decl_elementContext) elementContext).unbound_decls());
                inputDeclarations.add(decl);
            } else if (elementContext instanceof WdlDraft2Parser.Wf_inner_elementContext) {
                elements.add(visitChildren(elementContext));
            } else if (elementContext instanceof WdlDraft2Parser.Wf_output_elementContext) {
                outputs = visitWorkflow_output(((WdlDraft2Parser.Wf_output_elementContext) elementContext).workflow_output());
            } else if (elementContext instanceof WdlDraft2Parser.Wf_meta_elementContext) {
                meta = visitMeta(((WdlDraft2Parser.Wf_meta_elementContext) elementContext).meta());
            } else if (elementContext instanceof WdlDraft2Parser.Wf_parameter_meta_elementContext) {
                parameterMeta = visitParameter_meta(((WdlDraft2Parser.Wf_parameter_meta_elementContext) elementContext).parameter_meta());
            }
        }

        workflow.setInputs(Inputs.newBuilder().declarations(inputDeclarations).build());
        workflow.setOutputs(outputs);
        workflow.setElements(elements);
        workflow.setMeta(meta);
        workflow.setParameterMeta(parameterMeta);
        workflow.setId(id);
        return workflow;
    }

    @Override
    public WdlElement visitDocument_element(WdlDraft2Parser.Document_elementContext ctx) {
        return super.visitDocument_element(ctx);
    }

    @Override
    public Document visitDocument(WdlDraft2Parser.DocumentContext ctx) {
        int id = getNextId();
        Document document = new Document();
        List<Import> imports = new ArrayList<>();
        List<Struct> structs = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        Workflow workflow = ctx.workflow() != null ? visitWorkflow(ctx.workflow()) : null;

        document.setWorkflow(workflow);

        for (WdlDraft2Parser.Document_elementContext elementContext : ctx.document_element()) {
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
        document.setLib(new WdlDraft2StandardLib());
        document.setLanguageLevel(LanguageLevel.WDL_DRAFT_2);
        document.setCoercionOptions(new CoercionOptions(LanguageLevel.WDL_DRAFT_2));
        return document;
    }

}
