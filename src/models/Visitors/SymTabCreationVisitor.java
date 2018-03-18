package models.Visitors;

import models.AST.*;
import models.SymbolTable.*;

/**
 * Visitor to create symbol tables and their entries.  
 * 
 * This concerns only nodes that either:  
 * 
 * (1) represent identifier declarations/definitions, in which case they need to assemble 
 * a symbol table record to be inserted in a symbol table. These are:  VarDeclNode, ClassNode,  
 * and FuncDefNode. 
 * 
 * (2) represent a scope, in which case they need to create a new symbol table, and then 
 * insert the symbol table entries they get from their children. These are:  ProgNode, ClassNode, 
 * FuncDefNode, and StatBlockNode.   
 */

public class SymTabCreationVisitor extends Visitor {

	public void visit(ProgNode node){
		System.out.println("visiting ProgNode");
		node.symtab = new SymTab("global");
		// for classes, loop over all class declaration nodes
		for (Node classelt : node.getChildren().get(0).getChildren())
			//add the symbol table entry of each class in the global symbol table
			node.symtab.addEntry(classelt.symtabentry);
		// for function definitions, loop over all function definition nodes
		for (Node fndefelt : node.getChildren().get(1).getChildren())
			//add the symbol table entry of each function definition in the global symbol table
			node.symtab.addEntry(fndefelt.symtabentry);
		// for the program function, get its local symbol table from node 2 and create an entry for it in the global symbol table
		// first, get the table and change its name
		SymTab table = node.getChildren().get(2).symtab; 
		table.m_name = "program"; 
		node.symtab.addEntry("function:program", table);
	};

	public void visit(StatBlockNode node){
		System.out.println("visiting StatNode");
		node.symtab = new SymTab();
		// add the symbol table entries of all the variables declared in the statement block
		for (Node stat : node.getChildren()){
			if (stat.symtabentry != null) 
				node.symtab.addEntry(stat.symtabentry);
		}		
	};

	public void visit(FuncDefNode node){
		System.out.println("visiting FuncDefNode");
		String fname = node.getChildren().get(1).getData();
		node.symtab = new SymTab(fname);
		String declrecstring; 
		declrecstring = "function:";
		// function return value
		declrecstring += node.getChildren().get(0).getData() + ':';
		// function name
		declrecstring += node.getChildren().get(1).getData() + ':';
		// loop over function parameter list
		for (Node param : node.getChildren().get(2).getChildren()){
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
		SymTab table = node.getChildren().get(3).symtab; 
		table.m_name = node.getChildren().get(1).getData(); 
		node.symtabentry = new SymTabEntry(declrecstring, table);
		
		// add parameters of the function as local variables in the local symbol table
		for (Node param : node.getChildren().get(2).getChildren())
		node.symtabentry.m_subtable.addEntry(param.symtabentry);
	};
	
	public void visit(ClassNode node){
		System.out.println("visiting ClassNode");
		// get the class name from node 0
		String classname = node.getChildren().get(0).getData();
		// create a new table with the class name
		node.symtab = new SymTab(classname);
		// loop over all children of the class and migrate their symbol table entries in class table
		for (Node member : node.getChildren()){
			if (member.symtabentry != null) 
				node.symtab.addEntry(member.symtabentry);
		}
		// create the symbol table entry for the class
		node.symtabentry = new SymTabEntry("class:" + classname, node.symtab);
	};
	
	public void visit(VarDeclNode node){
		System.out.println("visiting VarDeclNode");
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
	}
}
