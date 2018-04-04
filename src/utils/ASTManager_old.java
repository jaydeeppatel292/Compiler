/*
package utils;

import models.AST.Node;
import models.ASTNode;
import models.Terminal;
import models.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ASTManager_old {
    private static ASTManager_old ourInstance = new ASTManager_old();
    private Stack<ASTNode> semanticStack = new Stack<>();

    public static ASTManager_old getInstance() {
        return ourInstance;
    }

    private ASTManager_old() {
    }

    public void takeSemanticAction(String x) {
//        System.out.println("Semantic Action:" + x);
        try {
            switch (x) {
                case "SEMANTIC_MAKE_NODE":
                    break;
                case "SEMANTIC_MAKE_FAMILY": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("inherList", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_PROG": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    Node classList;
                    Node funcDefList;
                    Node statBlock;
                    if (semanticStack.peek().nodeType.equals("statBlock")) {
                        */
/*classList = *//*
astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("funcDefList")) {
                        */
/*funcDefList = *//*
astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("classList")) {
                        */
/*statBlock = *//*
astNodeList.add(semanticStack.pop());
                    }

//                    Node node = new ProgNode(classList,funcDefList,statBlock);

                    makeFamily("prog", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_CLASSLIST": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("classList", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FUNCTION_LIST": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("funcDef")) {
                        astNodeList.add(semanticStack.pop());
                        makeFamily("funcDefList", astNodeList);
                    }
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_CLASS_DECL": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("memberList")) {
                        astNodeList.add(semanticStack.pop());
                    }

                    if (semanticStack.peek().nodeType.equals("inherList")) {
                        astNodeList.add(semanticStack.pop());
                    }

                    astNodeList.add(semanticStack.pop()); //  id ....
                    makeFamily("classDecl", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_VAR_FUNC_DECL": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("funcDecl")) {
                        astNodeList.add(semanticStack.pop());
                    }

                    if (semanticStack.peek().nodeType.equals("varDecl")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (astNodeList.size() > 0) {
                        makeFamily("memberList", astNodeList);
                    }
                    break;

                }
                case "SEMANTIC_MAKE_FAMILY_INHER_LIST": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("inherList", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_SR_ID": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("scopeSpec", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_DIM_LIST": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("dimList", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FPARAM_LIST": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("fparam")) {
                        astNodeList.add(semanticStack.pop());
                        makeFamily("fparmList", astNodeList);
                    }
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_INDEX_LIST": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("indexList", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_A_PARAMS": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("aParams", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_INDICE": {
                    transferFromOneFamilyToOther(semanticStack.peek(), "indice");
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FACTOR_NUM": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("num", astNodeList);
                    transferFromOneFamilyToOther(semanticStack.peek(), "factor");
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FACTOR": {
                    transferFromOneFamilyToOther(semanticStack.peek(), "factor");
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FACTOR_ARITH_EXPR": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("factor", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FACTOR_NOT": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("not", astNodeList);
                    transferFromOneFamilyToOther(semanticStack.peek(), "factor");
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FACTOR_SIGN": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("sign", astNodeList);
                    transferFromOneFamilyToOther(semanticStack.peek(), "factor");
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_VAR_DECL": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("dimList")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    astNodeList.add(semanticStack.pop());
                    astNodeList.add(semanticStack.pop());
                    makeFamily("varDecl", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FUNC_DECL": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("fparmList")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    astNodeList.add(semanticStack.pop());
                    astNodeList.add(semanticStack.pop());
                    makeFamily("funcDecl", astNodeList);
                    break;
                }

                case "SEMANTIC_MAKE_FAMILY_FPARAM": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("dimList")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    astNodeList.add(semanticStack.pop());
                    astNodeList.add(semanticStack.pop());
                    makeFamily("fparam", astNodeList);
                    break;
                }

                case "SEMANTIC_MAKE_FAMILY_EXPR": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("arithExpr")) {
                        astNodeList.add(semanticStack.pop());
                        makeFamily("expr", astNodeList);
                        break;
                    }
                    if (semanticStack.peek().nodeType.equals("relExpr")) {
                        astNodeList.add(semanticStack.pop());
                        makeFamily("expr", astNodeList);
                        break;
                    }
                    if (semanticStack.peek().nodeType.equals("factor")) {
                        astNodeList.add(semanticStack.pop());
                        makeFamily("expr", astNodeList);
                        break;
                    }
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FUNC_DEF": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("statBlock")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("fparmList")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("scopeSpec")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    astNodeList.add(semanticStack.pop()); // ID
                    astNodeList.add(semanticStack.pop()); // Type

                    makeFamily("funcDef", astNodeList);

                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_VAR": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("var", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_TERM": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("term") || semanticStack.peek().nodeType.equals("factor")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("term") || semanticStack.peek().nodeType.equals("factor")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    makeFamily("term", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_ARITH_EXPR": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    astNodeList.add(semanticStack.pop());
                    makeFamily("arithExpr", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_REL_EXPR": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    astNodeList.add(semanticStack.pop());
                    makeFamily("relExpr", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_DATA_MEMBER": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("indexList")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    astNodeList.add(semanticStack.pop());
                    makeFamily("dataMember", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_F_CALL": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("aParams")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    astNodeList.add(semanticStack.pop());
                    makeFamily("fCall", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_IF_STAT": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("statBlock")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("statBlock")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("expr")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    makeFamily("ifStat", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_FOR_STAT": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("statBlock")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("assignStat")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("relExpr")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("expr")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    astNodeList.add(semanticStack.pop());
                    astNodeList.add(semanticStack.pop());
                    makeFamily("forStat", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_STAT_BODY": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    if (semanticStack.peek().nodeType.equals("statBlock")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("statement")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    if (semanticStack.peek().nodeType.equals("varDecl")) {
                        astNodeList.add(semanticStack.pop());
                    }
                    makeFamily("statBlock", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_GET_STAT": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("getStat", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_PUT_STAT": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("putStat", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_RETURN_STAT": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("returnStat", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_STATEMENT": {
                    transferFromOneFamilyToOther(semanticStack.peek(), "statement");
                    */
/*List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("statement", astNodeList);*//*

                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_STATEMENT_BLOCK": {
                    transferFromOneFamilyToOther(semanticStack.peek(), "statBlock");
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_ASSIGN_LEFT_VAR": {
                    transferFromOneFamilyToOther(semanticStack.peek(), "assignLeftVar");
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_ST_BLOCK": {
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    makeFamily("statBlock", astNodeList);
                    break;
                }
                case "SEMANTIC_MAKE_FAMILY_ASSIGN_STAT": {
                    //TODO
                    List<ASTNode> astNodeList = new ArrayList<>();
                    astNodeList.add(semanticStack.pop());
                    astNodeList.add(semanticStack.pop());
                    makeFamily("assignStat", astNodeList);
                    break;
                }

                case "SEMANTIC_MAKE_SIBLING_VAR_ELEMENT": {
                    if (semanticStack.size() > 1) {
                        ASTNode astNode = semanticStack.pop();
                        if (semanticStack.peek().nodeType.equals("dataMember") || semanticStack.peek().nodeType.equals("fCall")) {
                            */
/*semanticStack.peek().sibling = astNode;
                            astNode.parent = semanticStack.peek().parent;*//*

                            makeSibling(semanticStack.peek(),astNode);
                        } else {
                            semanticStack.push(astNode);
                        }
                    }
                    break;
                }
                case "SEMANTIC_MAKE_SIBLING": {
                    if (semanticStack.size() > 1) {
                        ASTNode astNode = semanticStack.pop();
                        */
/*semanticStack.peek().sibling = astNode;
                        astNode.parent = semanticStack.peek().parent;*//*

                        makeSibling(semanticStack.peek(),astNode);

                    }
                    break;
                }
                case "SEMANTIC_MAKE_SIBLING_AR":
                case "SEMANTIC_MAKE_SIBLING_VAR_DECL":
                case "SEMANTIC_MAKE_SIBLING_COMMON": {
                    if (semanticStack.size() > 1) {
                        ASTNode astNode = semanticStack.pop();
                        if (astNode.nodeType.equals(semanticStack.peek().nodeType)) {
                            */
/*semanticStack.peek().sibling = astNode;
                            astNode.parent = semanticStack.peek().parent;*//*

                            makeSibling(semanticStack.peek(),astNode);

                        } else {
                            semanticStack.push(astNode);
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void makeSibling(ASTNode astNode,ASTNode newSibling){
        ASTNode currentAstNode = astNode;
        while (currentAstNode.sibling!=null){
            currentAstNode = currentAstNode.sibling;
        }
        currentAstNode.sibling = newSibling;
        newSibling.parent = currentAstNode.parent;

    }
    public void transferFromOneFamilyToOther(ASTNode astNode, String newName) {
        astNode.data = astNode.nodeType;
        astNode.nodeType = newName;
    }

    public void makeFamily(String familyName, List<ASTNode> astNodeList) {
        ASTNode astNode = new ASTNode(familyName);
        for (int index = astNodeList.size() - 1; index >= 0; index--) {
            ASTNode childNode = astNodeList.get(index);
            childNode.parent = astNode;
            if (index > 0) {
                childNode.sibling = astNodeList.get(index - 1);
            }
        }
        if (astNodeList.size() > 1) {
            astNode.firstChild = astNodeList.get(astNodeList.size() - 1);
        }
        semanticStack.push(astNode);
    }

    public void makeNode(String data, String nodeType) {
        ASTNode astNode = new ASTNode(nodeType, data);
        semanticStack.push(astNode);
    }

    public void makeNode(String tokenType, Token token) {
        try {
            if (tokenType.equals(Terminal.INTEGER.getData()) || tokenType.equals(Terminal.INT.getData()) || tokenType.equals(Terminal.id.getData()) || tokenType.equals(Terminal.FLOAT.getData())) {
                ASTNode astNode = new ASTNode(tokenType, token.getTokenValue());
                semanticStack.push(astNode);
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }
}
*/
