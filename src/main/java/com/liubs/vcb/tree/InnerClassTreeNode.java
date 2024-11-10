package com.liubs.vcb.tree;

import com.intellij.icons.AllIcons;
import org.objectweb.asm.tree.InnerClassNode;

import javax.swing.*;


/**
 * @author Liubsyy
 * @date 2024/10/23
 */
public class InnerClassTreeNode extends BaseTreeNode{
    private InnerClassNode innerClassNode;
    public InnerClassTreeNode(InnerClassNode innerClassNode) {
        super(innerClassNode.innerName);
        this.innerClassNode = innerClassNode;
    }

    @Override
    public Icon icon() {
        return  AllIcons.Nodes.Class;
    }


    public InnerClassNode getInnerClassNode() {
        return innerClassNode;
    }
}
