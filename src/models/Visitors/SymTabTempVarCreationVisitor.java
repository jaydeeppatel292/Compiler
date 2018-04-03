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

    public void visit(IdNode p_node) {
        p_node.m_moonVarName = p_node.getData();
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
}
