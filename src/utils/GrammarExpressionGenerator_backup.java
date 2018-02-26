/*
package utils;

import models.GrammarExpression;

import java.util.ArrayList;
import java.util.List;

public class GrammarExpressionGenerator_backup {

    //SEMANTIC_MAKE_NODE
    //SEMANTIC_MAKE_FAMILY
//    SEMANTIC_MAKE_SIBLING
    public static List<GrammarExpression> generateGrammarExpression() {
        List<GrammarExpression> grammarExpressionList = new ArrayList<>();
        grammarExpressionList.add(new GrammarExpression("prog", "cd fd program funcBody ;"));
        grammarExpressionList.add(new GrammarExpression("cd", "classDecl cd"));
        grammarExpressionList.add(new GrammarExpression("cd", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("fd", "funcDef fd"));
        grammarExpressionList.add(new GrammarExpression("fd", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("classDecl", "class id oid SEMANTIC_MAKE_FAMILY_INHER_LIST { varfundecl } ; SEMANTIC_MAKE_FAMILY_CLASS_DECL"));
        grammarExpressionList.add(new GrammarExpression("oid", ": id mid"));
        grammarExpressionList.add(new GrammarExpression("oid", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("mid", ", id SEMANTIC_MAKE_SIBLING mid"));
        grammarExpressionList.add(new GrammarExpression("mid", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("varfundecl", "type id varFunTail"));
        grammarExpressionList.add(new GrammarExpression("varfundecl", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("varFunTail", "ar ; varfundecl"));
        grammarExpressionList.add(new GrammarExpression("varFunTail", "( fParams ) ; funcDeclRep"));
        grammarExpressionList.add(new GrammarExpression("funcDeclRep", "funcDecl funcDeclRep"));
        grammarExpressionList.add(new GrammarExpression("funcDeclRep", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("ar", "arraySize ar"));
        grammarExpressionList.add(new GrammarExpression("ar", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("type", "int"));
        grammarExpressionList.add(new GrammarExpression("type", "float"));
        grammarExpressionList.add(new GrammarExpression("type", "id"));
        grammarExpressionList.add(new GrammarExpression("arraySize", "[ integer ]"));
        grammarExpressionList.add(new GrammarExpression("idsrid", "sr id"));
        grammarExpressionList.add(new GrammarExpression("idsrid", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("funcBody", "{ VDST }"));
        grammarExpressionList.add(new GrammarExpression("VDST", "id VDSTPrime"));
        grammarExpressionList.add(new GrammarExpression("VDST", "int id ar ; VDST"));
        grammarExpressionList.add(new GrammarExpression("VDST", "float id ar ; VDST"));
        grammarExpressionList.add(new GrammarExpression("VDST", "statementPrime ST"));
        grammarExpressionList.add(new GrammarExpression("VDST", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("VDSTPrime", "id ar ; VDST"));
        grammarExpressionList.add(new GrammarExpression("VDSTPrime", "variableTail assignOp expr ; ST"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "if ( expr ) then statBlock else statBlock ;"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "forBlock"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "get ( variable ) ;"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "put ( expr ) ;"));
        grammarExpressionList.add(new GrammarExpression("statementPrime", "return ( expr ) ;"));
        grammarExpressionList.add(new GrammarExpression("ST", "statement ST"));
        grammarExpressionList.add(new GrammarExpression("ST", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("funcDef", "funcHead funcBody ;"));
        grammarExpressionList.add(new GrammarExpression("funcHead", "type id idsrid ( fParams )"));
        grammarExpressionList.add(new GrammarExpression("funcDecl", "type id ( fParams ) ;"));
        grammarExpressionList.add(new GrammarExpression("fParams", "type id ar fpTail"));
        grammarExpressionList.add(new GrammarExpression("fParams", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("fpTail", "fParamsTail fpTail"));
        grammarExpressionList.add(new GrammarExpression("fpTail", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("fParamsTail", ", type id ar"));
        grammarExpressionList.add(new GrammarExpression("statement", "assignStat ;"));
        grammarExpressionList.add(new GrammarExpression("statement", "if ( expr ) then statBlock else statBlock ;"));
        grammarExpressionList.add(new GrammarExpression("statement", "forBlock"));
        grammarExpressionList.add(new GrammarExpression("statement", "get ( variable ) ;"));
        grammarExpressionList.add(new GrammarExpression("statement", "put ( expr ) ;"));
        grammarExpressionList.add(new GrammarExpression("statement", "return ( expr ) ;"));
        grammarExpressionList.add(new GrammarExpression("forBlock", "for ( type id assignOp expr ; relExpr ; assignStat ) statBlock ;"));
        grammarExpressionList.add(new GrammarExpression("assignStat", "variable assignOp expr"));
        grammarExpressionList.add(new GrammarExpression("statBlock", "{ ST }"));
        grammarExpressionList.add(new GrammarExpression("statBlock", "statement"));
        grammarExpressionList.add(new GrammarExpression("statBlock", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("expr", "arithExpr exprTail"));
        grammarExpressionList.add(new GrammarExpression("exprTail", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("exprTail", "relOp arithExpr"));
        grammarExpressionList.add(new GrammarExpression("relExpr", "arithExpr relOp arithExpr"));
        grammarExpressionList.add(new GrammarExpression("arithExpr", "term arithExprTAIL"));
        grammarExpressionList.add(new GrammarExpression("arithExprTAIL", "addOp term arithExprTAIL"));
        grammarExpressionList.add(new GrammarExpression("arithExprTAIL", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("term", "factor termTail"));
        grammarExpressionList.add(new GrammarExpression("termTail", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("termTail", "multOp factor termTail"));
        grammarExpressionList.add(new GrammarExpression("factor", "factorTemp"));
        grammarExpressionList.add(new GrammarExpression("factor", "integer"));
        grammarExpressionList.add(new GrammarExpression("factor", "float"));
        grammarExpressionList.add(new GrammarExpression("factor", "( arithExpr )"));
        grammarExpressionList.add(new GrammarExpression("factor", "not factor"));
        grammarExpressionList.add(new GrammarExpression("factor", "sign factor"));
        grammarExpressionList.add(new GrammarExpression("factorTemp", "id factorPrime"));
        grammarExpressionList.add(new GrammarExpression("factorPrime", "indiceRep factorTempA"));
        grammarExpressionList.add(new GrammarExpression("factorPrime", "( aParams ) factorTempA"));
        grammarExpressionList.add(new GrammarExpression("factorTempA", ". factorTemp"));
        grammarExpressionList.add(new GrammarExpression("factorTempA", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("indiceRep", "indice indiceRep"));
        grammarExpressionList.add(new GrammarExpression("indiceRep", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("indice", "[ arithExpr ]"));
        grammarExpressionList.add(new GrammarExpression("variable", "id variableTail"));
        grammarExpressionList.add(new GrammarExpression("variableTail", "indiceRep variablePrime"));
        grammarExpressionList.add(new GrammarExpression("variableTail", "( aParams ) . variable"));
        grammarExpressionList.add(new GrammarExpression("variablePrime", ". variable"));
        grammarExpressionList.add(new GrammarExpression("variablePrime", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("aParams", "expr aParamsTailRep"));
        grammarExpressionList.add(new GrammarExpression("aParams", "EPSILON"));
        grammarExpressionList.add(new GrammarExpression("aParamsTailRep", "aParamsTail aParamsTailRep"));
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
*/
