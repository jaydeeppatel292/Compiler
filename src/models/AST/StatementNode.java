package models.AST;

import models.Visitors.Visitor;

import java.util.List;

public class StatementNode extends Node {

	public StatementNode(){
		super("");
	}


	public StatementNode(Node statementNode){
		super("");
		this.addChild(statementNode);
	}
	
	/**
	 * If a visitable class contains members that also may need 
	 * to be visited, it should make these members to accept
	 * the visitor before itself being visited. 
	 */
	public void accept(Visitor visitor) {
		for (Node child : this.getChildren() )
			child.accept(visitor);
		visitor.visit(this);
	}
}
