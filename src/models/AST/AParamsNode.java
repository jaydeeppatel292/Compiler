package models.AST;

import models.Visitors.Visitor;

import java.util.List;

public class AParamsNode extends Node {
    public AParamsNode() {
        super("");
    }

    public AParamsNode(Node parent){
        super("", parent);
    }

    public AParamsNode(List<Node> listOfInherNodes){
        super("");
        for (Node child : listOfInherNodes)
            this.addChild(child);
    }

    /**
     * If a visitable class contains members that also may need
     * to be visited, it should make these members to accept
     * the visitor before itself being visited.
     */
    public void accept(Visitor visitor) {
        for (Node child : this.getChildren())
            child.accept(visitor);
        visitor.visit(this);
    }


}
