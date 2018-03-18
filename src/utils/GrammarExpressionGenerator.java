package utils;

import models.GrammarExpression;

import java.util.ArrayList;
import java.util.List;

public class GrammarExpressionGenerator {

    public static List<GrammarExpression> generateGrammarExpression() {
        List<GrammarExpression> grammarExpressionList = new ArrayList<>();
        grammarExpressionList.add(new GrammarExpression("prog", "cd SEMANTIC_MAKE_FAMILY_CLASSLIST fd SEMANTIC_MAKE_FAMILY_FUNCTION_LIST program funcBody ; SEMANTIC_MAKE_FAMILY_PROG"));
        grammarExpressionList.add(new GrammarExpression("cd", "classDecl SEMANTIC_MAKE_SIBLING_COMMON cd"));
        grammarExpressionList.add(new GrammarExpression("cd", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("fd", "funcDef SEMANTIC_MAKE_SIBLING_COMMON fd"));
        grammarExpressionList.add(new GrammarExpression("fd", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("classDecl", "class id SEMANTIC_MAKE_NODE_CLASS_ID oid SEMANTIC_MAKE_FAMILY_INHER_LIST { varfundecl SEMANTIC_MAKE_FAMILY_VAR_FUNC_DECL } ; SEMANTIC_MAKE_FAMILY_CLASS_DECL"));
        grammarExpressionList.add(new GrammarExpression("oid", ": id mid"));
        grammarExpressionList.add(new GrammarExpression("oid", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("mid", ", id SEMANTIC_MAKE_SIBLING mid"));
        grammarExpressionList.add(new GrammarExpression("mid", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("varfundecl", "type id varFunTail"));
        grammarExpressionList.add(new GrammarExpression("varfundecl", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("varFunTail", "ar SEMANTIC_MAKE_FAMILY_DIM_LIST ; SEMANTIC_MAKE_FAMILY_VAR_DECL SEMANTIC_MAKE_SIBLING_VAR_DECL varfundecl"));
        grammarExpressionList.add(new GrammarExpression("varFunTail", "( fParams ) ; SEMANTIC_MAKE_FAMILY_FUNC_DECL SEMANTIC_MAKE_SIBLING_COMMON funcDeclRep"));
        grammarExpressionList.add(new GrammarExpression("funcDeclRep", "funcDecl SEMANTIC_MAKE_SIBLING_COMMON funcDeclRep"));
        grammarExpressionList.add(new GrammarExpression("funcDeclRep", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("ar", "arraySize SEMANTIC_MAKE_SIBLING_AR ar"));
        grammarExpressionList.add(new GrammarExpression("ar", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("type", "int"));
        grammarExpressionList.add(new GrammarExpression("type", "float"));
        grammarExpressionList.add(new GrammarExpression("type", "id"));
        grammarExpressionList.add(new GrammarExpression("arraySize", "[ integer ]"));
        grammarExpressionList.add(new GrammarExpression("idsrid", "sr id SEMANTIC_MAKE_FAMILY_SR_ID"));
        grammarExpressionList.add(new GrammarExpression("idsrid", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("funcBody", "{ VDST } SEMANTIC_MAKE_FAMILY_STAT_BODY"));
        grammarExpressionList.add(new GrammarExpression("VDST", "id VDSTPrime"));
        grammarExpressionList.add(new GrammarExpression("VDST", "int id ar SEMANTIC_MAKE_FAMILY_DIM_LIST ; SEMANTIC_MAKE_FAMILY_VAR_DECL SEMANTIC_MAKE_SIBLING_VAR_DECL VDST"));
        grammarExpressionList.add(new GrammarExpression("VDST", "float id ar SEMANTIC_MAKE_FAMILY_DIM_LIST ; SEMANTIC_MAKE_FAMILY_VAR_DECL SEMANTIC_MAKE_SIBLING_VAR_DECL VDST"));
        grammarExpressionList.add(new GrammarExpression("VDST", "statementPrime ST"));
        grammarExpressionList.add(new GrammarExpression("VDST", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("VDSTPrime", "id ar SEMANTIC_MAKE_FAMILY_DIM_LIST ; SEMANTIC_MAKE_FAMILY_VAR_DECL SEMANTIC_MAKE_SIBLING_VAR_DECL VDST"));
        grammarExpressionList.add(new GrammarExpression("VDSTPrime", "variableTail SEMANTIC_MAKE_FAMILY_ASSIGN_LEFT_VAR assignOp expr ; SEMANTIC_MAKE_FAMILY_ASSIGN_STAT SEMANTIC_MAKE_FAMILY_STATEMENT ST"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "if ( expr ) then statBlock else statBlock ; SEMANTIC_MAKE_FAMILY_IF_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "forBlock SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "get ( variable ) ; SEMANTIC_MAKE_FAMILY_GET_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "put ( expr ) ; SEMANTIC_MAKE_FAMILY_PUT_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "return ( expr ) ; SEMANTIC_MAKE_FAMILY_RETURN_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("ST", "statement SEMANTIC_MAKE_SIBLING_COMMON ST"));
        grammarExpressionList.add(new GrammarExpression("ST", "EPSILON SEMANTIC_MAKE_FAMILY_ST_BLOCK"));
        grammarExpressionList.add(new GrammarExpression("funcDef", "funcHead funcBody ; SEMANTIC_MAKE_FAMILY_FUNC_DEF"));
        grammarExpressionList.add(new GrammarExpression("funcHead", "type id idsrid ( fParams )"));
        grammarExpressionList.add(new GrammarExpression("funcDecl", "type id ( fParams ) ; SEMANTIC_MAKE_FAMILY_FUNC_DECL"));
        grammarExpressionList.add(new GrammarExpression("fParams", "type id ar SEMANTIC_MAKE_FAMILY_DIM_LIST SEMANTIC_MAKE_FAMILY_FPARAM fpTail SEMANTIC_MAKE_FAMILY_FPARAM_LIST"));
        grammarExpressionList.add(new GrammarExpression("fParams", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("fpTail", "fParamsTail SEMANTIC_MAKE_FAMILY_FPARAM SEMANTIC_MAKE_SIBLING_COMMON fpTail"));
        grammarExpressionList.add(new GrammarExpression("fpTail", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("fParamsTail", ", type id ar SEMANTIC_MAKE_FAMILY_DIM_LIST"));
        grammarExpressionList.add(new GrammarExpression("statement", "assignStat ; SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statement", "if ( expr ) then statBlock else statBlock ; SEMANTIC_MAKE_FAMILY_IF_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statement", "forBlock SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statement", "get ( variable ) ; SEMANTIC_MAKE_FAMILY_GET_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statement", "put ( expr ) ; SEMANTIC_MAKE_FAMILY_PUT_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("statement", "return ( expr ) ; SEMANTIC_MAKE_FAMILY_RETURN_STAT SEMANTIC_MAKE_FAMILY_STATEMENT"));
        grammarExpressionList.add(new GrammarExpression("forBlock", "for ( type id assignOp expr ; relExpr ; assignStat ) statBlock ; SEMANTIC_MAKE_FAMILY_FOR_STAT"));
        grammarExpressionList.add(new GrammarExpression("assignStat", "variable SEMANTIC_MAKE_FAMILY_ASSIGN_LEFT_VAR assignOp expr SEMANTIC_MAKE_FAMILY_ASSIGN_STAT"));
        grammarExpressionList.add(new GrammarExpression("statBlock", "{ ST } SEMANTIC_MAKE_FAMILY_STATEMENT_BLOCK"));
        grammarExpressionList.add(new GrammarExpression("statBlock", "statement SEMANTIC_MAKE_FAMILY_STATEMENT_BLOCK"));
        grammarExpressionList.add(new GrammarExpression("statBlock", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("expr", "arithExpr exprTail SEMANTIC_MAKE_FAMILY_EXPR"));
        grammarExpressionList.add(new GrammarExpression("exprTail", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("exprTail", "relOp arithExpr SEMANTIC_MAKE_FAMILY_REL_EXPR"));
        grammarExpressionList.add(new GrammarExpression("relExpr", "arithExpr relOp arithExpr SEMANTIC_MAKE_FAMILY_REL_EXPR"));
        grammarExpressionList.add(new GrammarExpression("arithExpr", "term arithExprTAIL SEMANTIC_MAKE_FAMILY_ARITH_EXPR_FINAL"));
        grammarExpressionList.add(new GrammarExpression("arithExprTAIL", "addOp term SEMANTIC_MAKE_FAMILY_ARITH_EXPR arithExprTAIL"));
        grammarExpressionList.add(new GrammarExpression("arithExprTAIL", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("term", "factor termTail SEMANTIC_MAKE_FAMILY_TERM"));
        grammarExpressionList.add(new GrammarExpression("termTail", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("termTail", "multOp factor SEMANTIC_MAKE_FAMILY_MULT_OP termTail"));
        grammarExpressionList.add(new GrammarExpression("factor", "factorTemp SEMANTIC_MAKE_FAMILY_FACTOR"));
        grammarExpressionList.add(new GrammarExpression("factor", "integer SEMANTIC_MAKE_FAMILY_FACTOR_NUM"));
        grammarExpressionList.add(new GrammarExpression("factor", "float SEMANTIC_MAKE_FAMILY_FACTOR_NUM"));
        grammarExpressionList.add(new GrammarExpression("factor", "( arithExpr ) SEMANTIC_MAKE_FAMILY_FACTOR_ARITH_EXPR"));
        grammarExpressionList.add(new GrammarExpression("factor", "not factor SEMANTIC_MAKE_FAMILY_FACTOR_NOT"));
        grammarExpressionList.add(new GrammarExpression("factor", "sign factor SEMANTIC_MAKE_FAMILY_FACTOR_SIGN"));
        grammarExpressionList.add(new GrammarExpression("factorTemp", "id factorPrime"));
        grammarExpressionList.add(new GrammarExpression("factorPrime", "indiceRep SEMANTIC_MAKE_FAMILY_DATA_MEMBER SEMANTIC_MAKE_FAMILY_VAR_ELEMENT factorTempA"));
        grammarExpressionList.add(new GrammarExpression("factorPrime", "( aParams ) SEMANTIC_MAKE_FAMILY_F_CALL SEMANTIC_MAKE_FAMILY_VAR_ELEMENT factorTempA"));
        grammarExpressionList.add(new GrammarExpression("factorTempA", ". factorTemp"));
        grammarExpressionList.add(new GrammarExpression("factorTempA", "EPSILON SEMANTIC_MAKE_FAMILY_VAR"));
        grammarExpressionList.add(new GrammarExpression("indiceRep", "indice SEMANTIC_MAKE_SIBLING_COMMON indiceRep SEMANTIC_MAKE_FAMILY_INDEX_LIST"));
        grammarExpressionList.add(new GrammarExpression("indiceRep", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("indice", "[ arithExpr ] SEMANTIC_MAKE_FAMILY_INDICE"));
        grammarExpressionList.add(new GrammarExpression("variable", "id variableTail"));
        grammarExpressionList.add(new GrammarExpression("variableTail", "indiceRep SEMANTIC_MAKE_FAMILY_DATA_MEMBER SEMANTIC_MAKE_FAMILY_VAR_ELEMENT variablePrime"));
        grammarExpressionList.add(new GrammarExpression("variableTail", "( aParams ) SEMANTIC_MAKE_FAMILY_F_CALL SEMANTIC_MAKE_FAMILY_VAR_ELEMENT . variable"));
        grammarExpressionList.add(new GrammarExpression("variablePrime", ". variable"));
        grammarExpressionList.add(new GrammarExpression("variablePrime", "EPSILON SEMANTIC_MAKE_FAMILY_VAR"));
        grammarExpressionList.add(new GrammarExpression("aParams", "expr aParamsTailRep SEMANTIC_MAKE_FAMILY_A_PARAMS"));
        grammarExpressionList.add(new GrammarExpression("aParams", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("aParamsTailRep", "aParamsTail SEMANTIC_MAKE_SIBLING_COMMON aParamsTailRep"));
        grammarExpressionList.add(new GrammarExpression("aParamsTailRep", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("aParamsTail", ", expr"));
        grammarExpressionList.add(new GrammarExpression("addOp", "+"));
        grammarExpressionList.add(new GrammarExpression("addOp", "-"));
        grammarExpressionList.add(new GrammarExpression("addOp", "or"));
        grammarExpressionList.add(new GrammarExpression("relOp", "eq"));
        grammarExpressionList.add(new GrammarExpression("relOp", "neq"));
        grammarExpressionList.add(new GrammarExpression("relOp", "lt"));
        grammarExpressionList.add(new GrammarExpression("relOp", "gt"));
        grammarExpressionList.add(new GrammarExpression("relOp", "leq"));
        grammarExpressionList.add(new GrammarExpression("relOp", "geq"));
        grammarExpressionList.add(new GrammarExpression("sign", "+"));
        grammarExpressionList.add(new GrammarExpression("sign", "-"));
        grammarExpressionList.add(new GrammarExpression("multOp", "*"));
        grammarExpressionList.add(new GrammarExpression("multOp", "/"));
        grammarExpressionList.add(new GrammarExpression("multOp", "and"));
        grammarExpressionList.add(new GrammarExpression("assignOp", "="));
        return grammarExpressionList;
    }
}
