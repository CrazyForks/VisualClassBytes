package com.liubs.vcb.ui.common;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class IndexLinkLabel extends EditableLabel {
    private String indexText;

    public IndexLinkLabel(OnclickListener onClickListener) {
        super(true);
        jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickListener.actionPerform();
            }
        });
    }

    public String getText(){
        return indexText;
    }

    public void setText(String text) {
        this.indexText = text;
        jLabel.setText(String.format("<html><span style=\"color: #5799EE;\">[%s]</span></html>",text));
    }

}
