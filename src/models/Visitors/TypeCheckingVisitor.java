package models.Visitors;
import models.AST.*;

/**
 * Visitor to compute the type of subexpressions and assignment statements. 
 * 
 * This applies only to nodes that are part of expressions and assignment statements i.e.
 * AddOpNode, MultOpNode, and AssignStatNode. 
 * 
 */

public class TypeCheckingVisitor extends Visitor {

	public void visit(AddOpNode node){
		System.out.println("Visiting AddOppNode");
		String leftOperandType  = node.getChildren().get(0).getType();
		String rightOperandType = node.getChildren().get(1).getType();
		if( leftOperandType == rightOperandType )
			node.setType(leftOperandType);
		else{
			node.setType("typeerror");
			System.out.println("TYPE ERROR DETECTED between " 
					+ node.getChildren().get(0).getData()
					+  " and "
					+ node.getChildren().get(1).getData()
					);
		}
	}

	public void visit(MultOpNode node){ 
		System.out.println("Visiting MultOpNode");
		String leftOperandType  = node.getChildren().get(0).getType();
		String rightOperandType = node.getChildren().get(1).getType();
		if( leftOperandType == rightOperandType )
			node.setType(leftOperandType);
		else{
			node.setType("typeerror");
			System.out.println("TYPE ERROR DETECTED between " 
								+ node.getChildren().get(0).getData()
								+  " and "
								+ node.getChildren().get(1).getData()
								);
		}
	}
	
	public void visit(AssignStatNode node){ 
		System.out.println("Visiting AssignStatNode");
		String leftOperandType  = node.getChildren().get(0).getType();
		String rightOperandType = node.getChildren().get(1).getType();
		if( leftOperandType == rightOperandType )
			node.setType(leftOperandType);
		else{
			node.setType("typeerror");
			System.out.println("TYPE ERROR DETECTED between " 
								+ node.getChildren().get(0).getData()
								+  " and "
								+ node.getChildren().get(1).getData()
								);
		}
	}
}
