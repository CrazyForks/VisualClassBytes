package com.liubs.vcb.tree;

import com.intellij.icons.AllIcons;
import com.liubs.vcb.entity.MyInstructionInfo;
import com.liubs.vcb.aggregate.MyAssemblyMethod;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class MethodTreeNode extends BaseTreeNode{
    private MyAssemblyMethod assemblyMethod;
    public MethodTreeNode(MyAssemblyMethod assemblyMethod) {
        super(assemblyMethod.name());
        this.assemblyMethod = assemblyMethod;
    }

    @Override
    public Icon icon() {
        return AllIcons.Nodes.Method;
    }


    public MyInstructionInfo getInstructionInfo(){
        return assemblyMethod.buildInstructionInfo();
    }

    public MethodNode getMethodNode() {
        return assemblyMethod.getMethodNode();
    }

    public MyAssemblyMethod getAssemblyMethod() {
        return assemblyMethod;
    }
}
