package com.liubs.vcb.ui.constpool;


import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.tree.ConstantTreeNode;
import com.liubs.vcb.ui.ClassEditorPanel;
import com.liubs.vcb.ui.ConstPoolPanel;
import com.liubs.vcb.ui.MyTree;
import com.liubs.vcb.ui.common.EditableLabel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class ConstBasePanel  {
    private EditableLabel overview = new EditableLabel(true);

    private FormBuilder formBuilder;
    private JPanel panel;

    private ClassEditorPanel editorPanel;
    private ConstPoolPanel constPoolPanel;

    public ConstBasePanel() {
        formBuilder = FormBuilder.createFormBuilder().setVerticalGap(8);
        addLabeledComponent("Overview",overview);
    }

    public void addLabeledComponent(@NotNull String labelText, @NotNull JComponent component ) {
        formBuilder.addLabeledComponent(labelText,component);
    }

    public JPanel getPanel(){
        if(null == panel) {
            panel = formBuilder.getPanel();
            Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(etchedBorder, "Constant Info"),
                    JBUI.Borders.empty(8)));
        }
        return panel;
    }

    public void refresh(MyAssemblyConst myAssemblyConst){
        overview.setText(myAssemblyConst.getOverview());
    }

    public void setEditorPanel(ClassEditorPanel editorPanel) {
        this.editorPanel = editorPanel;
    }

    public void setConstPoolPanel(ConstPoolPanel constPoolPanel) {
        this.constPoolPanel = constPoolPanel;
    }

    public ClassEditorPanel getEditorPanel() {
        return editorPanel;
    }

    public void selectConstantNode(int index){
        if(null != editorPanel) {
            MyTree myTree = editorPanel.getMyTree();
            ConstantTreeNode constantTreeNode = myTree.getConstantTreeNodeMap().get(index);
            if(null != constantTreeNode) {
                myTree.selectNode(constantTreeNode);
            }
        }
        if(null != constPoolPanel){
            constPoolPanel.putStep(index);
        }
    }


}
