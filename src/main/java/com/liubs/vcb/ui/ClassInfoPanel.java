package com.liubs.vcb.ui;


import com.intellij.openapi.project.Project;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.liubs.vcb.constant.AccessConstant;
import com.liubs.vcb.tree.ClassInfoTreeNode;
import com.liubs.vcb.ui.common.EditableLabel;
import com.liubs.vcb.ui.validator.NumberInputValidator;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Liubsyy
 * @date 2024/10/23
 */
public class ClassInfoPanel extends JPanel implements IPanelRefresh<ClassInfoTreeNode> {
    private Project project;

    private EditableLabel minorVersion = new EditableLabel();
    private EditableLabel majorVersion = new EditableLabel();

    private EditableLabel superName = new EditableLabel();
    private EditableLabel access = new EditableLabel();

    private EditableLabel name = new EditableLabel();

    private EditableLabel sourceFile = new EditableLabel();
    private EditableLabel sourceDebug = new EditableLabel();


    public ClassInfoPanel(Project project) {
        this.project = project;
        setLayout(new BorderLayout());

        JPanel baseInfo = FormBuilder.createFormBuilder()
                .setVerticalGap(8)
                .addLabeledComponent("Minor Version : ", minorVersion)
                .addLabeledComponent("Major Version : ", majorVersion)
                .addLabeledComponent("Access : ", access)
                .addLabeledComponent("Name : ", name)
                .addLabeledComponent("Super : ", superName)
                .addLabeledComponent("Source File : ", sourceFile)
                .addLabeledComponent("Source Debug : ", sourceDebug)
                .getPanel();

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        baseInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(etchedBorder, "Class Info"),
                JBUI.Borders.empty(8)));

        this.add(baseInfo,BorderLayout.NORTH);

        initEditAction();
    }


    private ClassNode classNode;

    private void initEditAction(){
        minorVersion.onActionForInput("Minor version", NumberInputValidator.INSTANCE, r -> {
            classNode.version = (Integer.parseInt(r) << 16 & 0xFFFF0000) | (classNode.version & 0xFFFF);
        });
        majorVersion.onActionForInput("Major version", NumberInputValidator.INSTANCE, r->{
            classNode.version = (classNode.version & 0xFFFF0000) | (Integer.parseInt(r) & 0xFFFF);
        });

        access.onActionForCheckAccessBox("Class access", AccessConstant.CLASS_FLAGS,
                ()->classNode.access,
                r-> {
                    classNode.access = r;
                    refreshCurrentClassNode();
                });

        superName.onActionForInput("Super name",null,r->{
            classNode.superName = r;
        });
        name.onActionForInput("Class name",null,r->{
            classNode.name = r;
        });
        sourceFile.onActionForInput("Source file",null,r->{
            classNode.sourceFile = r;
        });
        sourceDebug.onActionForInput("Source debug",null,r->{
            classNode.sourceDebug = r;
        });
    }

    @Override
    public void refresh(ClassInfoTreeNode classInfoTreeNode) {
        classNode = classInfoTreeNode.getClassNode();
        refreshCurrentClassNode();
    }

    private void refreshCurrentClassNode(){
        if(null != classNode) {
            minorVersion.setText(String.valueOf(classNode.version>>16 & 0xFFFF));
            majorVersion.setText(String.valueOf(classNode.version & 0xFFFF));
            access.setText(String.format("0x%04x(%s)", classNode.access, String.join(" ", AccessConstant.getClassFlagNames(classNode.access))));
            name.setText(classNode.name);
            superName.setText(classNode.superName);
            sourceFile.setText(classNode.sourceFile);
            sourceDebug.setText(classNode.sourceDebug);
        }
    }
}
