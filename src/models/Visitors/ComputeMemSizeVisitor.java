package models.Visitors;

import models.AST.*;
import models.SymbolTable.*;
import models.Terminal;
import utils.ASTManager;
import utils.LexicalResponseManager;

import java.io.File;
import java.io.PrintWriter;

import static models.SymbolTable.SymTabEntry.SymbolDataType.CLASS;
import static models.SymbolTable.SymTabEntry.SymbolDataType.FLOAT;
import static models.SymbolTable.SymTabEntry.SymbolDataType.INT;

/**
 */

public class ComputeMemSizeVisitor extends Visitor {

    public String m_outputfilename = new String();

    public ComputeMemSizeVisitor() {
    }

    public ComputeMemSizeVisitor(String p_filename) {
        this.m_outputfilename = p_filename;
    }


    public int getSizeOfClassObject(String className) {
        int size = 0;
        Node progNode = ASTManager.getInstance().getProgNode();
        for (SymTabEntry symTabEntry : progNode.symtab.m_symlist) {
            if (symTabEntry.symbolType == SymTabEntry.SymbolType.CLASS && symTabEntry.m_subtable != null && symTabEntry.m_subtable.m_name.equals(className) ) {
                if(symTabEntry.m_subtable.m_size>0){
                    return symTabEntry.m_subtable.m_size;
                }
                return calculateMemSizeOfClassNode(symTabEntry);
            }
        }
        return size;
    }

    public int calculateMemSizeOfClassNode(SymTabEntry symTabEntry){
        int size=0;
        for (SymTabEntry classSymTabEntry : symTabEntry.m_subtable.m_symlist) {
            if (classSymTabEntry.symbolDataType != CLASS) {
                size += sizeOfEntry(classSymTabEntry);
            } else {
                size += getSizeOfClassObject(classSymTabEntry.extraData);
            }
        }
        symTabEntry.m_subtable.m_size = size;
        return size;
    }


    public int sizeOfEntry(Node p_node) {
        int size = 0;
        if (p_node.getType().equals(Terminal.INT.getData()) || p_node.symtabentry.symbolDataType == INT)
            size = 4;
        else if (p_node.getType().equals(Terminal.FLOAT.getData()) || p_node.symtabentry.symbolDataType == FLOAT)
            size = 8;

        // if it is an array, multiply by all dimension sizes
        if (p_node.symtabentry.varDimensionSize > 0) {
            for (Node dim : p_node.getChildren().get(2).getChildren()) {
                size *= Integer.parseInt(dim.getData());
            }
        }
        return size;
    }
    public int sizeOfEntry(SymTabEntry p_nodeTabEntry) {
        int size = 0;
        if ((p_nodeTabEntry.symbolDataType == INT))
            size = 4;
        else if ((p_nodeTabEntry.symbolDataType == FLOAT))
            size = 8;
        // if it is an array, multiply by all dimension sizes
        if (p_nodeTabEntry.varDimensionSize > 0) {
            for (int dim : p_nodeTabEntry.dimList) {
                size *= (dim);
            }
        }
        return size;
    }

    public int sizeOfTypeNode(Node p_node) {
        int size = 0;
        if (p_node.getType().equals(Terminal.INT.getData()) || p_node.symtabentry.symbolDataType == INT)
            size = 4;
        else if (p_node.getType().equals(Terminal.FLOAT.getData()) || p_node.symtabentry.symbolDataType == FLOAT)
            size = 8;
        return size;
    }

    public void visit(ProgNode p_node) {
        SymTab table = p_node.getChildren().get(2).symtab;
        for (SymTabEntry entry : table.m_symlist) {
            entry.m_offset = p_node.symtab.m_size - entry.m_size;
            p_node.symtab.m_size -= entry.m_size;
        }

        System.out.println(p_node.symtab);
        if (!this.m_outputfilename.isEmpty()) {
            File file = new File(this.m_outputfilename);
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(p_node.symtab);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void visit(ClassNode p_node) {
        // compute total size and offsets along the way
        // this should be node on all nodes that represent
        // a scope and contain their own table
        for (SymTabEntry entry : p_node.symtab.m_symlist) {
            entry.m_offset = p_node.symtab.m_size - entry.m_size;
            p_node.symtab.m_size -= entry.m_size;
        }
    }

    public void visit(FuncDefNode p_node) {
        // compute total size and offsets along the way
        // this should be node on all nodes that represent
        // a scope and contain their own table
        // stack frame contains the return value at the bottom of the stack
        p_node.symtab.m_size = -(this.sizeOfTypeNode(p_node.getChildren().get(0)));
        //then is the return addess is stored on the stack frame
        p_node.symtab.m_size -= 4;
        for (SymTabEntry entry : p_node.symtab.m_symlist) {
            entry.m_offset = p_node.symtab.m_size - entry.m_size;
            p_node.symtab.m_size -= entry.m_size;
        }
    }

    public void visit(VarDeclNode p_node) {
        // determine the size for basic variables

        switch (p_node.symtabentry.symbolDataType) {
            case FLOAT:
            case INT:
                p_node.symtabentry.m_size = this.sizeOfEntry(p_node);
                break;
            case CLASS:
                int size = getSizeOfClassObject(p_node.symtabentry.extraData);
                if (p_node.symtabentry.varDimensionSize > 0) {
                    for (Node dim : p_node.getChildren().get(2).getChildren()) {
                        size *= Integer.parseInt(dim.getData());
                    }
                }
                p_node.symtabentry.m_size =size;
                break;
        }

    }

    public void visit(MultOpNode p_node) {
        p_node.symtabentry.m_size = this.sizeOfEntry(p_node);
    }

    public void visit(AddOpNode p_node) {
        p_node.symtabentry.m_size = this.sizeOfEntry(p_node);
    }

    // Below are the visit methods for node types for which this visitor does
    // not apply. They still have to propagate acceptance of the visitor to
    // their children.

    public void visit(NumNode p_node) {
        p_node.symtabentry.m_size = this.sizeOfEntry(p_node);
    }

    public void visit(FCallNode p_node) {
        p_node.symtabentry.m_size = this.sizeOfEntry(p_node);
    }
}
