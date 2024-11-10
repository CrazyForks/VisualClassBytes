package com.liubs.vcb.tree;

import com.intellij.icons.AllIcons;

import javax.swing.*;

/**
 * @author Liubsyy
 * @date 2024/11/9
 */
public class ConstantPoolCategory extends BaseTreeNode{
    public ConstantPoolCategory() {
        super("Constant Pool");
    }

    @Override
    public Icon icon() {
        return AllIcons.Nodes.Constant;
    }
}
