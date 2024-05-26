package net.apple70cents.assistant70cents.features;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    String command;
    List<String> arguments;
    List<ASTNode> children;

    ASTNode(String command) {
        this.command = command;
        this.arguments = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    void addArgument(String arg) {
        this.arguments.add(arg);
    }

    void addChild(ASTNode child) {
        this.children.add(child);
    }

    @Override
    public String toString() {
        return "ASTNode{" + "command='" + command + '\'' + ", arguments=" + arguments + ", children=" + children + '}';
    }

    private void prettyPrint(String indent) {
        System.out.print(indent + command);
        if (!arguments.isEmpty()) {
            System.out.print(" " + arguments);
        }
        System.out.println();
        for (ASTNode child : children) {
            child.prettyPrint(indent + "    ");
        }
    }

    public void prettyPrint() {
        prettyPrint("");
    }
}
