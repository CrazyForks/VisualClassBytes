package com.liubs.visual.classbytes.ui;

import com.intellij.icons.AllIcons;
import com.liubs.visual.classbytes.tree.BaseTreeNode;

import javax.swing.*;
import java.awt.*;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class MyTreeCellRenderer extends JLabel implements javax.swing.tree.TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        BaseTreeNode node = (BaseTreeNode) value;

        // 设置节点文本
        setText(node.getUserObject().toString());

        //设置图标
        Icon icon = node.icon();
        if(icon == null){
            icon = leaf ? AllIcons.FileTypes.Any_type : AllIcons.Nodes.Folder ;
        }
        setIcon(icon);

        return this;
    }
}
