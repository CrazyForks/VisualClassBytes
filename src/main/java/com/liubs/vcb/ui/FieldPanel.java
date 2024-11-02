package com.liubs.vcb.ui;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.liubs.vcb.constant.AccessConstant;
import com.liubs.vcb.tree.FieldTreeNode;
import com.liubs.vcb.ui.common.EditableLabel;
import org.objectweb.asm.tree.FieldNode;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * @author Liubsyy
 * @date 2024/10/23
 */
public class FieldPanel extends JPanel implements IPanelRefresh<FieldTreeNode> {
    private Project project;

    //access
    private EditableLabel access = new EditableLabel();

    //name
    private EditableLabel name = new EditableLabel();

    //desc
    private EditableLabel desc = new EditableLabel();

    private JLabel value = new JLabel();


    public FieldPanel(Project project) {
        this.project = project;

        setLayout(new BorderLayout());

        JPanel baseInfo = FormBuilder.createFormBuilder()
                .setVerticalGap(8)
                .addLabeledComponent("Access : ", access)
                .addLabeledComponent("Name : ", name)
                .addLabeledComponent("Desc : ", desc)
                .addLabeledComponent("Value : ", value)
                .getPanel();

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        baseInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(etchedBorder, "Field Info"),
                JBUI.Borders.empty(8)));

        this.add(baseInfo,BorderLayout.NORTH);


        access.onActionForCheckAccessBox("Field access", AccessConstant.FIELD_FLAGS,
                ()->fieldNode.access,
                r-> {
                    fieldNode.access = r;
                    refreshCurrentNode();
                });
        name.onActionForInput("Field name",null,r->{
            fieldNode.name = r;
        });
        desc.onActionForInput("Field desc",null,r->{
            fieldNode.desc = r;
        });
    }

    private FieldNode fieldNode;
    @Override
    public void refresh(FieldTreeNode fieldTreeNode) {
        fieldNode = fieldTreeNode.getFieldNode();
        refreshCurrentNode();
    }

    public void refreshCurrentNode(){
        if(null == fieldNode) {
            return;
        }
        access.setText(String.format("0x%04x(%s)", fieldNode.access, String.join(" ", AccessConstant.getFieldsFlagNames(fieldNode.access))));
        name.setText(fieldNode.name);
        desc.setText(fieldNode.desc);

        value.setText(null == fieldNode.value ? null : String.valueOf(fieldNode.value));
    }
}
