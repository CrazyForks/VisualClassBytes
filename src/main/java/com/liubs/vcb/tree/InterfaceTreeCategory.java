package com.liubs.vcb.tree;

import com.intellij.icons.AllIcons;

import javax.swing.*;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class InterfaceTreeCategory extends BaseTreeNode{
    public InterfaceTreeCategory() {
        super("Interface");
    }

    @Override
    public Icon icon() {
        return AllIcons.Nodes.Interface;
    }
}
