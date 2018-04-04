package models.AST;

import models.Visitors.Visitor;

import java.util.List;

public class ParamListNode extends Node {
	
	public ParamListNode(){
		super("");
	}
	
	public ParamListNode(Node parent){
		super("", parent);
	}
	
	public ParamListNode(List<Node> listOfParamNodes){
		super("");
		for (Node child : listOfParamNodes)
			this.addChild(child);
	}
	
	/**
	 * If a visitable class contains members that also may need 
	 * to be visited, it should make these members to accept
	 * the visitor before itself being visited. 
	 */
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
