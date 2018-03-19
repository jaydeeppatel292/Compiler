package models.Visitors;

import models.AST.*;
import models.SymbolTable.SymTab;
import models.SymbolTable.SymTabEntry;
import models.Terminal;
import models.Token;
import models.TokenType;
import utils.ASTManager;
import utils.LexicalResponseManager;

/**
 * Visitor to compute the type of subexpressions and assignment statements.
 * <p>
 * This applies only to nodes that are part of expressions and assignment statements i.e.
 * AddOpNode, MultOpNode, and AssignStatNode.
 */

public class TypeCheckingVisitor extends Visitor {

    public void visit(AddOpNode node) {
        System.out.println("Visiting AddOppNode");
        String leftOperandType = node.getChildren().get(0).getType();
        String rightOperandType = node.getChildren().get(1).getType();
        if (leftOperandType == rightOperandType)
            node.setType(leftOperandType);
        else {
            node.setType("typeerror");
            System.out.println("TYPE ERROR DETECTED between "
                    + node.getChildren().get(0).getData()
                    + " and "
                    + node.getChildren().get(1).getData()
            );
        }
    }

    public void visit(FactorNode factorNode) {
        if (factorNode.getChildren().size() > 0) {
            factorNode.setType(factorNode.getChildren().get(0).getType());
            factorNode.setData(factorNode.getChildren().get(0).getData());
        }
    }

    public void visit(TermNode node) {
        if (node.getChildren().size() > 0) {
            node.setType(node.getChildren().get(0).getType());
            node.setData(node.getChildren().get(0).getData());
        }
    }
    public void visit(ExprNode node) {
        if (node.getChildren().size() > 0) {
            node.setType(node.getChildren().get(0).getType());
            node.setData(node.getChildren().get(0).getData());
        }
    }


    public void visit(ArithExprNode node) {
        System.out.println("Visiting ArithExprNode");

        // only term in arithexpr node ///
        if (node.getChildren().size() == 1) {
            node.setType(node.getChildren().get(0).getType());
            node.setData(node.getChildren().get(0).getData());
            return;
        }

        // multop as child ..
        String leftOperandType = node.getChildren().get(0).getType();
        String rightOperandType = node.getChildren().get(1).getType();
        if (leftOperandType == rightOperandType)
            node.setType(leftOperandType);
        else {
            node.setType("typeerror");
            System.out.println("TYPE ERROR DETECTED between "
                    + node.getChildren().get(0).getData()
                    + " and "
                    + node.getChildren().get(1).getData()
            );
        }
    }

    public void visit(MultOpNode node) {
        System.out.println("Visiting MultOpNode");
        String leftOperandType = node.getChildren().get(0).getType();
        String rightOperandType = node.getChildren().get(1).getType();
        if (leftOperandType == rightOperandType)
            node.setType(leftOperandType);
        else {
            node.setType("typeerror");
            System.out.println("TYPE ERROR DETECTED between "
                    + node.getChildren().get(0).getData()
                    + " and "
                    + node.getChildren().get(1).getData()
            );
        }
    }

    public void visit(AssignStatNode node) {
        System.out.println("Visiting AssignStatNode");
        String leftOperandType = node.getChildren().get(0).getType();
        String rightOperandType = node.getChildren().get(1).getType();
        if (leftOperandType == rightOperandType)
            node.setType(leftOperandType);
        else {
            node.setType("typeerror");
            System.out.println("TYPE ERROR DETECTED between "
                    + node.getChildren().get(0).getData()
                    + " and "
                    + node.getChildren().get(1).getData()
            );
        }
    }

    public void visit(VarNode node) {
        Node currentNode = node;
        SymTab currentSymTable = null;
        String nodeType = null;
        for (Node child : node.getChildren()) {
            String type = child.getType();
            switch (type.split("#")[0]) {
                case "DATA":
                    SymTabEntry varType = getTypeForVarFromSymbolTable(type, currentNode,currentSymTable);
                    if(varType==null){
                        node.setType("typeerror");
                        System.out.println("Semantic Error Could not able to find :"+type);
                        return;
                    }
                    if(!varType.extraData.equals("int")&& !varType.extraData.equals("float")){
                        currentSymTable = findSymbolTableForClass(varType.extraData);
                        if(currentSymTable==null){
                            node.setType("typeerror");
                            System.out.println("Semantic Error Class Not Found :"+varType.symbolName);
                            return;
                        }
                    }
                    else {
                        nodeType = varType.extraData;
                    }
                    break;
                case "FCALL":
                    break;
            }
        }
        if(nodeType!=null){
            node.setType(nodeType);
        }
    }

    public SymTab findSymbolTableForClass(String name) {
        Node progNode = ASTManager.getInstance().getProgNode();
        for(SymTabEntry symTabEntry : progNode.symtab.m_symlist) {
            if (symTabEntry.symbolType == SymTabEntry.SymbolType.CLASS && symTabEntry.m_subtable.m_name.equals(name)){
                return symTabEntry.m_subtable;
            }
        }
        return null;
    }

    public SymTabEntry getTypeForVarFromSymbolTable(String type, Node currentNode,SymTab symTab) {

        String typeArray[] = type.split("#");
        String varName = typeArray[1];
        int dimension = Integer.parseInt(typeArray[2]);
        Node searchNode = currentNode;

        if(symTab!=null){
            for (SymTabEntry symTabEntry : symTab.m_symlist) {
                if (symTabEntry.symbolType == SymTabEntry.SymbolType.VARIABLE || symTabEntry.symbolType == SymTabEntry.SymbolType.PARAMETER) {
                    if (symTabEntry.symbolName.equals(varName) && symTabEntry.varDimensionSize == dimension) {
                        return symTabEntry;
                    }
                }
            }
            return null;
        }


        while (searchNode.getParent() != null) {
            if (searchNode.symtab != null) {
                for (SymTabEntry symTabEntry : searchNode.symtab.m_symlist) {
                    if (symTabEntry.symbolType == SymTabEntry.SymbolType.VARIABLE || symTabEntry.symbolType == SymTabEntry.SymbolType.PARAMETER) {
                        if (symTabEntry.symbolName.equals(varName) && symTabEntry.varDimensionSize == dimension) {
                            return symTabEntry;
                        }
                    }
                }
            }
            searchNode = searchNode.getParent();
        }
        return null;
    }

    public void visit(VarElementNode node) {
        node.setType(node.getChildren().get(0).getType());
    }


    public void visit(IndexListNode node) {
        node.setType("" + node.getChildren().size());
        for(Node child :node.getChildren()){
            if(!child.getType().equals(Terminal.INT.getData())){
                LexicalResponseManager.getInstance().addErrorMessage(0,0,"SemanticError","Incompatible types: Required Int");
            }
        }
    }

    public void visit(AParamsNode node) {
        String type = "";
        for (Node child : node.getChildren()) {
            type += "_" + child.getType();
        }
        node.setType(type);
    }

    public void visit(DataMemberNode node) {
        if (node.getChildren().size() > 1) {
            node.setType("DATA#" + node.getChildren().get(0).getData() + "#" + node.getChildren().get(1).getType());
        }
    }

    public void visit(FCallNode node) {
        if (node.getChildren().size() > 1) {
            node.setType("FCALL#" + node.getChildren().get(0).getData() + "#" + node.getChildren().get(1).getType());
        }
    }
}
