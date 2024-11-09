package com.liubs.vcb.ui.common;


import javax.swing.*;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 多选框
 * @author Liubsyy
 * @date 2024/10/26
 */
public class MultiCheckedDialog extends DialogWrapper {
    private final List<JCheckBox> checkBoxes;
    private final JPanel panel;

    public MultiCheckedDialog(String title, String message, List<String> options, List<String> checkedOptions) {
        super(true); // 让DialogWrapper支持模态
        setTitle(title);
        checkBoxes = new ArrayList<>();
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel(message));
        panel.add(Box.createVerticalStrut(10));

        // 为每个选项创建一个 JCheckBox，并添加到面板
        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            if(null != checkedOptions && checkedOptions.contains(option)) {
                checkBox.setSelected(true);
            }
            checkBoxes.add(checkBox);

            panel.add(checkBox);
            panel.add(Box.createVerticalStrut(10));
        }


        init(); // 初始化对话框
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    public List<String> getSelectedOptions() {
        List<String> selectedOptions = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedOptions.add(checkBox.getText());
            }
        }
        return selectedOptions;
    }
    public List<Integer> getSelectIndexes(){
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0;i<checkBoxes.size() ;i++){
            JCheckBox checkBox  = checkBoxes.get(i);
            if(checkBox.isSelected()) {
                indexes.add(i);
            }
        }
        return indexes;
    }

}
