package models.AST;

import models.Visitors.Visitor;

public class ProgNode extends Node {
	
	/**
	 * Some intermediate nodes may contain additional members
	 * that store information aggregated by specific visitors.  
	 * Here, a variable declaration record is stored, which 
	 * stores information aggregated by the VarDeclVisitor. 
	 * In the project, this record would be added to a symbol 
	 * table.  
	 */
	
	public ProgNode(){
		super("");
	}
	
	public ProgNode(Node parent){
		super("", parent);
	}
	
	public ProgNode(Node classlist, Node funcdeflist, Node statblock){
		super(""); 
		this.addChild(classlist);
		this.addChild(funcdeflist);
		this.addChild(statblock);		
	}
	
	/**
	 * Every node should have an accept method, which 
	 * should call accept on its children to propagate
	 * the action of the visitor on its children. 
	 */
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
}