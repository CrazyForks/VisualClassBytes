package com.liubs.vcb.ui.common;

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
    public static final int H_GAP = 10;

    protected TruncatedLabel jLabel;
    protected JBLabel editAction;

    public EditableLabel(){
        this(false);
    }
    public EditableLabel(boolean readOnly) {
        jLabel = new TruncatedLabel();
        if(!readOnly) {
            editAction = new JBLabel(MyIcons.EDIT);
            editAction.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 设置手形指针
        }

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, H_GAP, 0);
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
        jLabel.setFullText(text);
    }

    public String getText() {
        return jLabel.getFullText();
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
                MultiCheckedDialog checkedMessageDialog = new MultiCheckedDialog("Edit Value",message,
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
                AccessDialog checkedMessageDialog = new AccessDialog("Edit Value",message,
                        accessFlags,access.get());
                if(checkedMessageDialog.showAndGet()) {
                    resultHandler.accept(checkedMessageDialog.getAccess());
                }
            }
        });
    }


    static class TruncatedLabel extends JLabel {
        private String fullText; // 用于存储完整的文本内容

        public TruncatedLabel() {
            super();
        }

        public void setFullText(String text) {
            this.fullText = text; // 保存完整文本
            updateDisplayedText(); // 更新显示内容
        }

        public String getFullText() {
            return fullText;
        }

        private void updateDisplayedText() {
            if(null == fullText) {
                super.setText(null);
                return;
            }
            if (fullText.length() > 80) {
                super.setText(fullText.substring(0, 80) + "...");
            } else {
                super.setText(fullText); // 不超过则显示完整内容
            }
        }
    }
}
