package com.liubs.vcb.tree;

import com.intellij.icons.AllIcons;
import com.liubs.vcb.domain.assemblycode.MyAssemblyField;
import org.objectweb.asm.tree.FieldNode;

import javax.swing.*;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class FieldTreeNode extends BaseTreeNode{
    private MyAssemblyField fieldNode;
    public FieldTreeNode(MyAssemblyField fieldNode) {
        super(fieldNode.name());
        this.fieldNode = fieldNode;
    }

    @Override
    public Icon icon() {
        return AllIcons.Nodes.Field;
    }

    public FieldNode getFieldNode() {
        return fieldNode.getFieldNode();
    }
}
