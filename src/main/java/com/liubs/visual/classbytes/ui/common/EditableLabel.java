package com.liubs.visual.classbytes.ui.common;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBLabel;
import icons.MyIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Liubsyy
 * @date 2024/10/24
 */
public class EditableLabel extends JPanel {
    private JLabel jLabel;
    private JBLabel editAction;

    public EditableLabel(){
        this(false);
    }
    public EditableLabel(boolean readOnly) {
        jLabel = new JLabel();
        if(!readOnly) {
            editAction = new JBLabel(MyIcons.EDIT);
            editAction.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 设置手形指针
        }

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 18, 0);
        setLayout(flowLayout);
        add(jLabel);
        if(!readOnly) {
            add(editAction);
        }

    }

    public void onClick(OnclickListener onClickListener){
        if(null == editAction) {
            return;
        }
        editAction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickListener.actionPerform();
            }
        });
    }


    public void setText(String text) {
        jLabel.setText(text);
    }

    public String getText() {
        return jLabel.getText();
    }


    /**
     * 输入框编辑文本
     * @param resultHandler
     */
    public void onActionForInput(String message, InputValidator inputValidator, Consumer<String> resultHandler ) {
        if(null == editAction) {
            return;
        }
        editAction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String inputValue = Messages.showInputDialog((Project)null,message,
                        "Edit Value", Messages.getInformationIcon(),
                        getText(),inputValidator);
                if(null != inputValue) {
                    resultHandler.accept(inputValue);
                    setText(inputValue);
                }
            }
        });
    }


    public void onActionForCheckedBox(String message, java.util.List<String> options, Supplier<List<String>> checkedOptions, Consumer<List<String>> resultHandler ){
        if(null == editAction) {
            return;
        }
        editAction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MultiCheckedMessageDialog checkedMessageDialog = new MultiCheckedMessageDialog("Edit Value",message,
                        options,checkedOptions.get());
                if(checkedMessageDialog.showAndGet()) {
                    resultHandler.accept(checkedMessageDialog.getSelectedOptions());
                }
            }
        });
    }

    public void onActionForCheckAccessBox(String message, Map<Integer,String> accessFlags, Supplier<Integer> access,Consumer<Integer> resultHandler){
        if(null == editAction) {
            return;
        }
        editAction.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AccessMessageDialog checkedMessageDialog = new AccessMessageDialog("Edit Value",message,
                        accessFlags,access.get());
                if(checkedMessageDialog.showAndGet()) {
                    resultHandler.accept(checkedMessageDialog.getAccess());
                }
            }
        });
    }
}
