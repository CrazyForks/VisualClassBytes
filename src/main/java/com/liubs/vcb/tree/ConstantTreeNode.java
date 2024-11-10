package com.liubs.vcb.tree;

import com.intellij.icons.AllIcons;
import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;

import javax.swing.*;

/**
 * @author Liubsyy
 * @date 2024/11/9
 */
public class ConstantTreeNode extends BaseTreeNode{
    private MyAssemblyConst myAssemblyConst;

    public ConstantTreeNode(MyAssemblyConst myConst) {
        super(myConst.getName());
        this.myAssemblyConst = myConst;
    }

    @Override
    public Icon icon() {
        return AllIcons.Nodes.Constant;
    }

    public MyAssemblyConst getMyAssemblyConst() {
        return myAssemblyConst;
    }
}
