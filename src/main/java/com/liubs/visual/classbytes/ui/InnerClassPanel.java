package com.liubs.visual.classbytes.ui;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.liubs.visual.classbytes.ui.common.EditableLabel;
import com.liubs.visual.classbytes.constant.AccessConstant;
import com.liubs.visual.classbytes.tree.InnerClassTreeNode;
import org.objectweb.asm.tree.InnerClassNode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Liubsyy
 * @date 2024/10/23
 */
public class InnerClassPanel extends JPanel implements IPanelRefresh<InnerClassTreeNode> {

    private Project project;

    private EditableLabel access = new EditableLabel();
    private EditableLabel name = new EditableLabel();
    private EditableLabel innerName = new EditableLabel();
    private EditableLabel outerName = new EditableLabel(true);

    public InnerClassPanel(Project project) {
        this.project = project;

        setLayout(new BorderLayout());

        JPanel baseInfo = FormBuilder.createFormBuilder()
                .setVerticalGap(8)
                .addLabeledComponent("Access : ", access)
                .addLabeledComponent("Full Name : ", name)
                .addLabeledComponent("Inner Name : ", innerName)
                .addLabeledComponent("Outer Name : ", outerName)
                .getPanel();

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        baseInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(etchedBorder, "Inner Class"),
                JBUI.Borders.empty(8)));

        this.add(baseInfo,BorderLayout.NORTH);

        initEditAction();
    }

    private void initEditAction() {

        access.onActionForCheckAccessBox("Inner class access", AccessConstant.CLASS_FLAGS,
                ()->innerClassNode.access,
                r-> {
                    innerClassNode.access = r;
                    refresh();
                });

        name.onActionForInput("Class name",null,r->{
            innerClassNode.name = r;
        });
        innerName.onActionForInput("Inner name",null,r->{
            innerClassNode.innerName = r;
        });

    }

    private InnerClassNode innerClassNode;

    @Override
    public void refresh(InnerClassTreeNode treeNode) {
        innerClassNode = treeNode.getInnerClassNode();
        refresh();
    }

    private void refresh(){
        if(innerClassNode == null){
            return;
        }
        access.setText(String.format("0x%04x(%s)", innerClassNode.access, String.join(" ", AccessConstant.getClassFlagNames(innerClassNode.access))));
        name.setText(innerClassNode.name);
        innerName.setText(innerClassNode.innerName);
        outerName.setText(innerClassNode.outerName);
    }
}
