package models.Visitors;
import models.AST.*;

/**
 * Visitor to construct a string that represents the subexpression
 * of the subtree for which the current node is the head. 
 * 
 * This applies only to nodes that are part of expressions, i.e.
 * IdNode, AddOpNode, MultOpNode, and AssignStatNode. 
 * 
 * Note that this is just as an example. Such functionality is not
 * required in the project. 
 * 
 * However, note that this is essentially how the code generation phase
 * will eventually proceed. 
 * 
 */

public class ConstructAssignmentAndExpressionStringVisitor extends Visitor {
	
	public void visit(IdNode node){ 
		System.out.println("Visiting IdNode");
		node.setSubtreeString(node.getData());
	}

	public void visit(AddOpNode node){
		System.out.println("Visiting AddOpNode");
		node.setSubtreeString(	node.getChildren().get(0).getSubtreeString() + 
								node.getData() +
								node.getChildren().get(1).getSubtreeString() );
	}

	public void visit(MultOpNode node){ 
		System.out.println("Visiting MultOpNode");
		node.setSubtreeString(	node.getChildren().get(0).getSubtreeString() + 
								node.getData() +
								node.getChildren().get(1).getSubtreeString() );
	}
	
	public void visit(AssignStatNode node){ 
		System.out.println("Visiting AssignStatNode");
		node.setSubtreeString(	node.getChildren().get(0).getSubtreeString() + 
								node.getData() +
								node.getChildren().get(1).getSubtreeString() );
	}
	
}
