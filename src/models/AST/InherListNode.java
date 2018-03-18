package models.AST;

import models.Visitors.Visitor;

import java.util.List;

public class InherListNode extends Node {
    public InherListNode() {
        super("");
    }

    public InherListNode(Node parent){
        super("", parent);
    }

    public InherListNode(List<Node> listOfInherNodes){
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
