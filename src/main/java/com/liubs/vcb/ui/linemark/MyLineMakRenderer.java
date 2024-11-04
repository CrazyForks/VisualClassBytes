package com.liubs.vcb.ui.linemark;


import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.util.Key;
import com.liubs.vcb.ui.common.TextIcon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * @author Liubsyy
 * @date 2024/10/20
 */
public class MyLineMakRenderer extends GutterIconRenderer {

    public static final Key<Boolean> LABEL_LINE = Key.create("liubsyy_vcb_label_line");

    private int labelIndex; //第几个Label
    private int editorLine; //编辑器所在行数
    private int sourceCodeLine; //源码行 linenumber

    private TextIcon textIcon;
    private String tooltipText;

    public MyLineMakRenderer(int labelIndex, int editorLine, int sourceCodeLine) {
        this.labelIndex = labelIndex;
        this.editorLine = editorLine;
        this.sourceCodeLine = sourceCodeLine;

        setTextIcon(labelIndex);
        setTooltipText(sourceCodeLine);
    }

    private void setTextIcon(int labelIndex){
        this.textIcon = new TextIcon("L"+labelIndex);
    }

    public void setTooltipText(int sourceCodeLine) {
        this.tooltipText = "Linenumber:"+sourceCodeLine;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return textIcon;
    }


    public void showModifyDialog(){
        LineMarkDialog dialog = new LineMarkDialog(labelIndex,sourceCodeLine);
        if(dialog.showAndGet()) {
            if(labelIndex != dialog.getIndex()){
                labelIndex = dialog.getIndex();
                setTextIcon(labelIndex);
            }
            if(sourceCodeLine != dialog.getSourceLine()) {
                sourceCodeLine = dialog.getSourceLine();
                setTooltipText(sourceCodeLine);
            }
        }
    }


    @Override
    public Alignment getAlignment() {
        return Alignment.RIGHT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyLineMakRenderer that = (MyLineMakRenderer) o;
        return labelIndex == that.labelIndex && editorLine == that.editorLine && sourceCodeLine == that.sourceCodeLine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelIndex, editorLine, sourceCodeLine);
    }

    @Override
    public String getTooltipText() {
        return tooltipText;
    }

    public int getLabelIndex() {
        return labelIndex;
    }

    public int getEditorLine() {
        return editorLine;
    }

    public int getSourceCodeLine() {
        return sourceCodeLine;
    }




}
