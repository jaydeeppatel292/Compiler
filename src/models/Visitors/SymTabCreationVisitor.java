package models.Visitors;

import models.AST.*;
import models.SymbolTable.*;
import models.Terminal;
import utils.ASTManager;
import utils.LexicalResponseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor to create symbol tables and their entries.
 * <p>
 * This concerns only nodes that either:
 * <p>
 * (1) represent identifier declarations/definitions, in which case they need to assemble
 * a symbol table record to be inserted in a symbol table. These are:  VarDeclNode, ClassNode,
 * and FuncDefNode.
 * <p>
 * (2) represent a scope, in which case they need to create a new symbol table, and then
 * insert the symbol table entries they get from their children. These are:  ProgNode, ClassNode,
 * FuncDefNode, and StatBlockNode.
 */

public class SymTabCreationVisitor extends Visitor {

    public void visit(ProgNode node) {
        System.out.println("visiting ProgNode");
        node.symtab = new SymTab("global");
        // for classes, loop over all class declaration nodes
        for (Node classelt : node.getChildren().get(0).getChildren()) {
            //add the symbol table entry of each class in the global symbol table
            if(classelt.symtabentry.symbolType == SymTabEntry.SymbolType.CLASS) {
                for (SymTabEntry symTab : node.symtab.m_symlist) {
                    try {
                        if (symTab.symbolType == SymTabEntry.SymbolType.CLASS && symTab.m_subtable.m_name.equals(classelt.symtabentry.m_subtable.m_name)) {
                            LexicalResponseManager.getInstance().addErrorMessage(-1,-1,"SemanticError","Class multiple declaration :"+symTab.m_subtable.m_name);
                        }
                    }catch (Exception ex){
                    }
                }
            }
            node.symtab.addEntry(classelt.symtabentry);
        }
        // for function definitions, loop over all function definition nodes
        for (Node fndefelt : node.getChildren().get(1).getChildren()) {
            //add the symbol table entry of each function definition in the global symbol table

            // check for scop spec
            if (fndefelt.getChildren().get(1).getData() == null || fndefelt.getChildren().get(1).getData().isEmpty()) {
                node.symtab.addEntry(fndefelt.symtabentry);
            }

            // if scope spec is available then add symbol table point to class funcdecl ..
            else {
                boolean isClassDeclFound = false;
                for (Node classelt : node.getChildren().get(0).getChildren()) {
                    if (classelt.getChildren().get(0).getData().equals(fndefelt.getChildren().get(1).getData())) {
                        for (SymTabEntry symTabEntry : classelt.symtab.m_symlist) {
                            if (symTabEntry.m_entry.contains("function") && fndefelt.symtabentry.m_entry.equals(symTabEntry.m_entry)) {
                                symTabEntry.m_entry = fndefelt.symtabentry.m_entry;
                                symTabEntry.m_subtable = fndefelt.symtabentry.m_subtable;
                                isClassDeclFound = true;
                            }
                        }
                    }
                }
                if (!isClassDeclFound) {
                    // Report symentic error func decl not found ...
                    fndefelt.generatePosition();
                    LexicalResponseManager.getInstance().addErrorMessage(node.lineNumber,node.colNumber,"SemanticError","Could not resolve symbol :"+fndefelt.getChildren().get(1).getData());
                }
            }
        }

        // add inherited symbol list in class symtab entry ::
        for (Node classNode : node.getChildren().get(0).getChildren()){
            for(Node inheritedNode : classNode.getChildren().get(1).getChildren()){
                for(SymTabEntry symTabEntry : node.symtab.m_symlist){
                    if(symTabEntry.symbolType== SymTabEntry.SymbolType.CLASS && symTabEntry.m_subtable !=null && symTabEntry.m_subtable.m_name.equals(inheritedNode.getData())){
                        // Circular dependence at first level: Check if already entry exist
                        if(classNode.symtabentry.inheritedSymTab.contains(symTabEntry.m_subtable)){
                            classNode.generatePosition();
                            LexicalResponseManager.getInstance().addErrorMessage(node.lineNumber,node.colNumber,"SemanticError","Circular class dependencies found for "+inheritedNode.getData() +" In class inheritance :"+classNode.getChildren().get(0).getData());
                        }else{
                            classNode.symtabentry.addInheritedSymTab(symTabEntry);
                        }
                    }
                }
            }
        }

        // check for circular class dependence at multi level
        for(SymTabEntry symTabEntry : node.symtab.m_symlist) {
            if(symTabEntry.symbolType== SymTabEntry.SymbolType.CLASS) {
                boolean isCircular=false;
                List<SymTabEntry> allInheritedSymTab = new ArrayList<>();
                for(SymTabEntry symTab : symTabEntry.inheritedSymTab){
                    List<SymTabEntry> symTabEntryList = new ArrayList<>();
                    symTabEntryList.add(symTab);
                    if(isCircularDependence(symTab,symTabEntryList)){
                        isCircular = true;
                    }
                    addUniqueSymTabFromInheritence(allInheritedSymTab,symTabEntryList);
                }
                symTabEntry.multiLevelInheritedSymTab = allInheritedSymTab;
                if(isCircular){
                    LexicalResponseManager.getInstance().addErrorMessage(0,0,"SemanticError","Circular class dependencies found In class inheritance :"+symTabEntry.m_entry);
                }
            }
        }


            // for the program function, get its local symbol table from node 2 and create an entry for it in the global symbol table
        // first, get the table and change its name
        SymTab table = node.getChildren().get(2).symtab;
        table.m_name = "program";
        node.symtab.addEntry("function:program", table);
        checkShadowedVarDecl(node.symtab);
    }
    ;

    public void addUniqueSymTabFromInheritence(List<SymTabEntry> intoSymTabEntryList,List<SymTabEntry> fromSymTabEntryList){
        for(SymTabEntry symTabEntry : fromSymTabEntryList){
            if(!intoSymTabEntryList.contains(symTabEntry)){
                intoSymTabEntryList.add(symTabEntry);
            }
        }
    }

    public void checkShadowedVarDecl(SymTab symTab){
        for(SymTabEntry tabEntry : symTab.m_symlist) {
            if(tabEntry.symbolType == SymTabEntry.SymbolType.CLASS) {
                for(SymTabEntry currentClassSymTab : tabEntry.m_subtable.m_symlist) {
                    for (SymTabEntry inheritedTabEntry : tabEntry.multiLevelInheritedSymTab) {
                        for (SymTabEntry inheritedTable : inheritedTabEntry.m_subtable.m_symlist) {
                            if (inheritedTable.m_entry.equals(currentClassSymTab.m_entry)) {
                                LexicalResponseManager.getInstance().addErrorMessage(0, 0, "Semantic Warning", "Shadowed variable::" + currentClassSymTab.m_entry + " in Class:"+tabEntry.m_subtable.m_name+" Already declared in:" + inheritedTabEntry.m_subtable.m_name);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isCircularDependence(SymTabEntry symTabEntry,List<SymTabEntry> classSymListTraversal){
        boolean isCircularDependence = false;
        for(SymTabEntry inheritedSymTab : symTabEntry.inheritedSymTab){
            if(classSymListTraversal.contains(inheritedSymTab)){
                return true;
            }
            classSymListTraversal.add(inheritedSymTab);
        }
        for(SymTabEntry inheritedSymTab : symTabEntry.inheritedSymTab) {
            if (isCircularDependence(inheritedSymTab, classSymListTraversal)) {
                return true;
            }
        }
        return isCircularDependence;
    }

    public void visit(StatBlockNode node) {
        System.out.println("visiting StatNode");
        node.symtab = new SymTab();


        // add the symbol table entries of all the variables declared in the statement block
        addAllSymbolInParentTable(node.symtab, node, node.getChildren());

        for (Node stat : node.getChildren()) {
			/*if (stat.symtabentry != null)
				node.symtab.addEntry(stat.symtabentry);*/
            if (stat.getNodeCategory().equals("statement") && stat.getChildren().get(0).getNodeCategory().equals("forStat")) {
                SymTab table = stat.getChildren().get(0).symtab;
                node.symtab.addEntry("For:", table);
            }
        }
    }

    ;

    public void visit(FuncDefNode node) {
        System.out.println("visiting FuncDefNode");
        String fname = node.getChildren().get(1).getData();
        node.symtab = new SymTab(fname);
        String declrecstring;
        declrecstring = "function:";
        // function return value
        declrecstring += node.getChildren().get(0).getData() + ':';
		/*// function scop spec
		declrecstring += node.getChildren().get(1).getData() + ':';*/
        // function name
        declrecstring += node.getChildren().get(2).getData() + ':';
        // loop over function parameter list
        String extraData="";
        for (Node param : node.getChildren().get(3).getChildren()) {
            extraData += param.getChildren().get(0).getData();
            if(param.getChildren().get(2).getChildren().size()>0) {
                extraData += ":" + param.getChildren().get(2).getChildren().size() ;
            }
            extraData +="_";
            // parameter type
            declrecstring += param.getChildren().get(0).getData() + ':';
            // parameter name
            declrecstring += param.getChildren().get(1).getData() + ':';
            // parameter dimension list
            for (Node dim : param.getChildren().get(2).getChildren())
                // parameter dimension
                declrecstring += dim.getData() + ':';
        }
        // the symbol table of the function is the symbol table of its statement block
        // first, get the table and adapt its name to the function
        SymTab table = node.getChildren().get(4).symtab;
        table.m_name = node.getChildren().get(2).getData();
        node.symtabentry = new SymTabEntry(declrecstring, table);

        // add parameters of the function as local variables in the local symbol table
        addAllSymbolInParentTable(node.symtabentry.m_subtable, node, node.getChildren().get(3).getChildren());

        node.symtabentry.extraData = extraData;
        node.symtabentry.symbolName = node.getChildren().get(2).getData();
        node.symtabentry.returnType = node.getChildren().get(0).getData();
        node.symtabentry.symbolType = SymTabEntry.SymbolType.FUNCTION;

		/*for (Node param : node.getChildren().get(3).getChildren())
		node.symtabentry.m_subtable.addEntry(param.symtabentry);*/
    }

    ;

    public void visit(ClassNode node) {
        System.out.println("visiting ClassNode");
        // get the class name from node 0
        String classname = node.getChildren().get(0).getData();
        // create a new table with the class name
        node.symtab = new SymTab(classname);
        // loop over all children of the class and migrate their symbol table entries in class table

        addAllSymbolInParentTable(node.symtab, node, node.getChildren().get(2).getChildren());

        // create the symbol table entry for the class
        node.symtabentry = new SymTabEntry("class:" + classname, node.symtab);
        node.symtabentry.symbolType = SymTabEntry.SymbolType.CLASS;
    }

    ;

    public void visit(ForStatNode node) {
        System.out.println("visiting ForStatNode");
        node.symtab = new SymTab("For");

        // aggregate information from the subtree
        String declrecstring;
        // identify what kind of record that is
        declrecstring = "localvar:";
        // get the type from the first child node and aggregate here
        declrecstring += node.getChildren().get(0).getData() + ':';
        // get the id from the second child node and aggregate here
        declrecstring += node.getChildren().get(1).getData() + ':';

        SymTabEntry  symTabEntry = new SymTabEntry(declrecstring);
        symTabEntry.extraData = node.getChildren().get(0).getData(); // type
        symTabEntry.symbolName = node.getChildren().get(1).getData(); // var name
        symTabEntry.symbolType = SymTabEntry.SymbolType.VARIABLE;

        node.symtab.addEntry(symTabEntry);
        node.symtabentry = new SymTabEntry("For:", node.symtab);

    }

    public void visit(VarDeclNode node) {
        System.out.println("visiting VarDeclNode");
        // aggregate information from the subtree
        String declrecstring;
        String type = "";
        // identify what kind of record that is
        declrecstring = "localvar:";
        // get the type from the first child node and aggregate here
        declrecstring += node.getChildren().get(0).getData() + ':';
        type += node.getChildren().get(0).getData();
        // get the id from the second child node and aggregate here
        declrecstring += node.getChildren().get(1).getData() + ':';
        // loop over the list of dimension nodes and aggregate here
        for (Node dim : node.getChildren().get(2).getChildren()) {
            declrecstring += dim.getData() + ':';
            type += "[" + dim.getData() + "]";
            if (!dim.getType().equals(Terminal.INT.getData())) {
                dim.generatePosition();
                LexicalResponseManager.getInstance().addErrorMessage(dim.lineNumber,dim.colNumber,"SemanticError","Incompatible types: Required Int: Found"+dim.getType());
            }
        }
        // create the symbol table entry for this variable
        // it will be picked-up by another node above later
        node.symtabentry = new SymTabEntry(declrecstring, null);
        node.setType(type);
        node.symtabentry.extraData = node.getChildren().get(0).getData(); // type
        node.symtabentry.symbolName = node.getChildren().get(1).getData(); // var name
        node.symtabentry.varDimensionSize = node.getChildren().get(2).getChildren().size();
        node.symtabentry.symbolType = SymTabEntry.SymbolType.VARIABLE;
    }

    public void visit(FuncDeclNode node) {
        System.out.println("visiting FuncDeclNode");
        String fname = node.getChildren().get(1).getData();
        node.symtab = new SymTab(fname);
        String declrecstring;
        declrecstring = "function:";
        // function return value
        declrecstring += node.getChildren().get(0).getData() + ':';
        // function name
        declrecstring += node.getChildren().get(1).getData() + ':';
        // loop over function parameter list
        String extraData = "";
        for (Node param : node.getChildren().get(2).getChildren()) {
            extraData += param.getChildren().get(0).getData() + "_" + param.getChildren().get(2).getChildren().size() + ":";
            // parameter type
            declrecstring += param.getChildren().get(0).getData() + ':';
            // parameter name
            declrecstring += param.getChildren().get(1).getData() + ':';
            // parameter dimension list
            for (Node dim : param.getChildren().get(2).getChildren())
                // parameter dimension
                declrecstring += dim.getData() + ':';
        }

        // the symbol table of the function is the symbol table of its statement block
        // first, get the table and adapt its name to the function
        node.symtabentry = new SymTabEntry(declrecstring, null);
        node.symtabentry.extraData = extraData;
        node.symtabentry.symbolName = node.getChildren().get(1).getData();
        node.symtabentry.returnType = node.getChildren().get(0).getData();
        node.symtabentry.symbolType = SymTabEntry.SymbolType.FUNCTION;
    }

    public void visit(FParamNode node) {
        System.out.println("visiting FParamNode");
        // aggregate information from the subtree
        String declrecstring;
        // identify what kind of record that is
        declrecstring = "localvar:";
        // get the type from the first child node and aggregate here
        declrecstring += node.getChildren().get(0).getData() + ':';
        // get the id from the second child node and aggregate here
        declrecstring += node.getChildren().get(1).getData() + ':';
        // loop over the list of dimension nodes and aggregate here
        for (Node dim : node.getChildren().get(2).getChildren())
            declrecstring += dim.getData() + ':';
        // create the symbol table entry for this variable
        // it will be picked-up by another node above later
        node.symtabentry = new SymTabEntry(declrecstring, null);
        node.symtabentry.extraData = node.getChildren().get(0).getData(); // type
        node.symtabentry.symbolName = node.getChildren().get(1).getData(); // var name
        node.symtabentry.varDimensionSize = node.getChildren().get(2).getChildren().size();
        node.symtabentry.symbolType = SymTabEntry.SymbolType.PARAMETER;

    }

    public void addAllSymbolInParentTable(SymTab symTab, Node parent, List<Node> childNodeList) {
        for (Node member : childNodeList) {
            if (member.symtabentry != null) {
                boolean isSymAlreadyDeclared = false;
                for (SymTabEntry symTabEntry : parent.symtab.m_symlist) {
                    if (symTabEntry.m_entry.equals(member.symtabentry.m_entry)) {
                        member.generatePosition();
                        LexicalResponseManager.getInstance().addErrorMessage(member.lineNumber,member.colNumber,"SemanticError","Multiple declaration of :" + member.symtabentry.m_entry);
                        System.out.println("Multiple declaration of :" + member.symtabentry.m_entry);
                        isSymAlreadyDeclared = true;
                    }
                }
                if (!isSymAlreadyDeclared) {
                    symTab.addEntry(member.symtabentry);
                }
            }
        }
    }

}
