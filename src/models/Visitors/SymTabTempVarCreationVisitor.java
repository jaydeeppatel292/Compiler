package models.Visitors;

import models.AST.*;
import models.SymbolTable.SymTab;
import models.SymbolTable.SymTabEntry;
import utils.LexicalResponseManager;

public class SymTabTempVarCreationVisitor extends Visitor {
    public Integer m_tempVarNum = 0;

    public String getNewTempVarName() {
        m_tempVarNum++;
        return "t" + m_tempVarNum.toString();
    }

    public void visit(AddOpNode p_node) {
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.symtabentry = new SymTabEntry(SymTabEntry.SymbolType.TEMPVAR, p_node.getType(), p_node.m_moonVarName);
        p_node.symtabentry.m_entry = "tempvar:" + tempvarname + " " + p_node.getType();
        SymTab symTab = p_node.findParentSymTab(p_node);
        if (symTab != null) {
            symTab.addEntry(p_node.symtabentry);
        }
    }

    @Override
    public void visit(AParamsNode node) {

    }

    public void visit(ArithExprNode p_node) {
        // only term in arithexpr node ///
        if (p_node.getChildren().size() == 1) {
            return;
        }

        // multop as child ..
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.symtabentry = new SymTabEntry(SymTabEntry.SymbolType.TEMPVAR, p_node.getType(), p_node.m_moonVarName);
        p_node.symtabentry.m_entry = "tempvar:" + tempvarname + " " + p_node.getType();
        SymTab symTab = p_node.findParentSymTab(p_node);
        if (symTab != null) {
            symTab.addEntry(p_node.symtabentry);
        }
    }

    @Override
    public void visit(AssignStatNode node) {

    }

    @Override
    public void visit(ClassListNode node) {

    }

    @Override
    public void visit(ClassNode node) {

    }

    @Override
    public void visit(DataMemberNode node) {

    }

    @Override
    public void visit(DimListNode node) {

    }

    @Override
    public void visit(ExprNode node) {

    }

    @Override
    public void visit(FactorNode node) {

    }

    @Override
    public void visit(FactorSignNode node) {

    }

    public void visit(MultOpNode p_node) {
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.symtabentry = new SymTabEntry(SymTabEntry.SymbolType.TEMPVAR, p_node.getType(), p_node.m_moonVarName);
        p_node.symtabentry.m_entry = "tempvar:" + tempvarname + " " + p_node.getType();
        SymTab symTab = p_node.findParentSymTab(p_node);
        if (symTab != null) {
            symTab.addEntry(p_node.symtabentry);
        }
    }

    @Override
    public void visit(Node node) {

    }

    public void visit(NumNode p_node) {
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        p_node.symtabentry = new SymTabEntry(SymTabEntry.SymbolType.LITVAL, p_node.getType(), p_node.m_moonVarName);
        p_node.symtabentry.m_entry = "litval:" + tempvarname + " " + p_node.getType();
        SymTab symTab = p_node.findParentSymTab(p_node);
        if (symTab != null) {
            symTab.addEntry(p_node.symtabentry);
        }
    }

    @Override
    public void visit(OpNode node) {

    }

    @Override
    public void visit(ParamListNode node) {

    }

    @Override
    public void visit(ProgNode node) {

    }

    @Override
    public void visit(PutStatNode node) {

    }

    @Override
    public void visit(RelExprNode node) {

    }

    @Override
    public void visit(ReturnStatNode node) {

    }

    @Override
    public void visit(ScopeSpecNode node) {

    }

    @Override
    public void visit(StatBlockNode node) {

    }

    @Override
    public void visit(StatementNode node) {

    }

    @Override
    public void visit(TermNode node) {

    }

    @Override
    public void visit(TypeNode node) {

    }

    @Override
    public void visit(VarDeclNode node) {

    }

    @Override
    public void visit(VarElementNode node) {

    }

    @Override
    public void visit(VarNode node) {

    }

    @Override
    public void visit(ProgramBlockNode node) {

    }

    public void visit(IdNode p_node) {
        p_node.m_moonVarName = p_node.getData();
    }

    @Override
    public void visit(IfStatNode node) {

    }

    @Override
    public void visit(IndexListNode node) {

    }

    @Override
    public void visit(InherListNode node) {

    }

    @Override
    public void visit(MemberListNode node) {

    }


    public void visit(FCallNode p_node) {
        String tempvarname = this.getNewTempVarName();
        p_node.m_moonVarName = tempvarname;
        String vartype = p_node.getType();
        p_node.symtabentry = new SymTabEntry(SymTabEntry.SymbolType.RETVAL, vartype, p_node.m_moonVarName);
        p_node.symtabentry.m_entry = "retval:" + tempvarname + " " + p_node.getType();
        SymTab symTab = p_node.findParentSymTab(p_node);
        if (symTab != null) {
            symTab.addEntry(p_node.symtabentry);
        }
    }

    @Override
    public void visit(ForStatNode node) {

    }

    @Override
    public void visit(FParamListNode node) {

    }

    @Override
    public void visit(FParamNode node) {

    }

    @Override
    public void visit(FuncDeclNode node) {

    }

    @Override
    public void visit(FuncDefListNode node) {

    }

    @Override
    public void visit(FuncDefNode node) {

    }

    @Override
    public void visit(GetStatNode node) {

    }
}
