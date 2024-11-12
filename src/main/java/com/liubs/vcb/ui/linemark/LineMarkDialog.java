package com.liubs.vcb.ui.linemark;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.liubs.vcb.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Liubsyy
 * @date 2024/11/4
 */
public class LineMarkDialog extends DialogWrapper {

    private JPanel mainPanel;

    private JBTextField index;
    private JBTextField sourceLine;

    protected LineMarkDialog(int labelIndex, int sourceCodeLine) {
        super((Project) null, true);
        setTitle("Line Label");

        index = new JBTextField();
        index.setPreferredSize(new Dimension(200, 30));
        if(labelIndex >=0) {
            index.setText("L"+labelIndex);
        }
        sourceLine = new JBTextField();
        sourceLine.setPreferredSize(new Dimension(200, 30));
        if(sourceCodeLine>=0) {
            sourceLine.setText(String.valueOf(sourceCodeLine));
        }

        mainPanel = FormBuilder.createFormBuilder()
                .setVerticalGap(10)
                .addLabeledComponent("Index : ", index)
                .addLabeledComponent("Line Number : ", sourceLine)
                .getPanel();
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected void doOKAction() {
        String textIndex = index.getText().replace("L", "");
        String sourceIndex = sourceLine.getText();
        if( !StringUtils.isNumeric(textIndex) || !StringUtils.isNumeric(sourceIndex)
                || Integer.parseInt(textIndex) <0 || Integer.parseInt(sourceIndex) < 0) {
            Messages.showErrorDialog("Invalid input for index or line number","Input Error");
            return;
        }
        super.doOKAction();
    }

    public int getIndex() {
        return Integer.parseInt(index.getText().replace("L",""));
    }

    public int getSourceLine() {
        return Integer.parseInt(sourceLine.getText());
    }
}
