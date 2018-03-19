package models.Visitors;

import models.AST.*;

/**
 * Visitor superclass. Can be either an interface or an abstract class.
 * Needs to have one visit method for each of the visit methods
 * implemented by any of its subclasses.
 *
 * This forces all its subclasses
 * to implement all of them, even if they are not concerned with
 * processing of this particular subtype, creating visit methods
 * with an empty function body.
 *
 * In order to avoid having empty visit functions, one can create
 * the Visitor class as a normal class with all visitor methods
 * with empty function bodies.
 *
 * These empty methods are executed in case a specific visitor does
 * not implement a visit method for a specific kind of visitable
 * object on which accept may be called, but no action is required
 * for this specific visitor.
 */

 public class Visitor {
	 public void visit(AddOpNode node)       	{};
	 public void visit(AssignStatNode node)  	{};
	 public void visit(ClassListNode node)   	{};
	 public void visit(ClassNode node)       	{};
	 public void visit(DimListNode node)     	{};
	 public void visit(FuncDefListNode node) 	{};
	 public void visit(FuncDefNode node)     	{};
	 public void visit(IdNode node)          	{};
	 public void visit(MultOpNode node)      	{};
	 public void visit(Node node)            	{};
	 public void visit(NumNode numNode)      	{};
	 public void visit(ProgNode node)        	{};
	 public void visit(StatBlockNode node)   	{};
	 public void visit(TypeNode node)        	{};
	 public void visit(VarDeclNode node)     	{};
	 public void visit(FParamNode node)      	{};
	 public void visit(FuncDeclNode node)    	{};
	 public void visit(ForStatNode node) 	 	{};
	 public void visit(ArithExprNode node)    	{};
	 public void visit(FactorNode node)		 	{};
	 public void visit(TermNode node)			{};
 	 public void visit(VarNode node)			{};
	 public void visit(VarElementNode node)		{};
	 public void visit(IndexListNode node)		{};
	 public void visit(AParamsNode node)		{};
	 public void visit(DataMemberNode node)		{};
	 public void visit(FCallNode node)			{};
	 public void visit(ExprNode node)			{};
	 public void visit(RelExprNode node)			{};

    }