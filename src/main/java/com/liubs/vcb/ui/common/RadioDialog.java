package com.liubs.vcb.ui.common;

import com.intellij.openapi.ui.DialogWrapper;
import com.liubs.vcb.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2024/11/9
 */
public class RadioDialog extends DialogWrapper {
    private List<JRadioButton> radioButtons = new ArrayList<>();
    private final JPanel panel = new JPanel();

    public RadioDialog(String title,String message, List<String> radioButtons) {
        super( true);
        setTitle(title);


        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10));

        if(StringUtils.isNotEmpty(message)) {
            panel.add(new JLabel(message));
            panel.add(Box.createVerticalStrut(20));
        }

        ButtonGroup group = new ButtonGroup();
        for(int i = 0,len=radioButtons.size() ; i<len ;i++) {
            JRadioButton radioButton = new JRadioButton(radioButtons.get(i));
            if(i == 0){
                radioButton.setSelected(true);
            }

            group.add(radioButton);
            this.radioButtons.add(radioButton);

            panel.add(radioButton);
            panel.add(Box.createVerticalStrut(10));
        }

        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return panel;
    }

    public int getSelectRadio(){
        for(int i = 0,len=radioButtons.size() ;i<len ; i++ ) {
            if(radioButtons.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }


}
