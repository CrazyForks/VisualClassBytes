package com.liubs.vcb.ui.linemark;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.liubs.vcb.entity.MyLineNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liubsyy
 * @date 2024/11/4
 */
public class LineMarkManager {

    public static void markLineLighter(Editor editor, List<MyLineNumber> lineNumberList){

        MarkupModel markupModel = editor.getMarkupModel();
        Document document = editor.getDocument();

        // 收集需要移除的 Highlighter
        List<RangeHighlighter> toRemove = new ArrayList<>();
        for (RangeHighlighter highlighter : markupModel.getAllHighlighters()) {
            if (Boolean.TRUE.equals(highlighter.getUserData(MyLineMakRenderer.LABEL_LINE))) {
                toRemove.add(highlighter);
            }
        }
        // 移除旧的 Highlighter
        for (RangeHighlighter highlighter : toRemove) {
            markupModel.removeHighlighter(highlighter);
        }

        for(MyLineNumber eachLine : lineNumberList) {
            int lineNumber = eachLine.getLineEditor();
            int lineStartOffset = document.getLineStartOffset(lineNumber);
            int lineEndOffset = document.getLineEndOffset(lineNumber);

            // 创建 RangeHighlighter
            RangeHighlighter highlighter = markupModel.addRangeHighlighter(
                    lineStartOffset,
                    lineEndOffset,
                    HighlighterLayer.FIRST,
                    null,
                    HighlighterTargetArea.LINES_IN_RANGE
            );

            // 设置自定义的 GutterIconRenderer
            highlighter.setGutterIconRenderer(new MyLineMakRenderer(eachLine.getLabelIndex(),lineNumber,eachLine.getLineSource()));
            highlighter.putUserData(MyLineMakRenderer.LABEL_LINE, Boolean.TRUE);
        }
    }

    public static List<MyLineNumber> getAllMarkedLines(Editor editor) {
        return getAllHighlighters(editor).stream().map(myRenderer->{
            MyLineNumber myLineNumber = new MyLineNumber();
            myLineNumber.setLabelIndex(myRenderer.getLabelIndex());
            myLineNumber.setLineEditor(myRenderer.getEditorLine());
            myLineNumber.setLineSource(myRenderer.getSourceCodeLine());
            return myLineNumber;
        }).collect(Collectors.toList());
    }
    public static List<MyLineMakRenderer> getAllHighlighters(Editor editor) {
        List<MyLineMakRenderer> highlighters = new ArrayList<>();
        MarkupModel markupModel = editor.getMarkupModel();

        for (RangeHighlighter highlighter : markupModel.getAllHighlighters()) {
            if (Boolean.TRUE.equals(highlighter.getUserData(MyLineMakRenderer.LABEL_LINE))) {
                GutterIconRenderer renderer = highlighter.getGutterIconRenderer();
                if (renderer instanceof MyLineMakRenderer) {
                    MyLineMakRenderer myRenderer = (MyLineMakRenderer) renderer;
                    highlighters.add(myRenderer);
                }
            }
        }
        return highlighters;
    }


    public static void showModifyDialog(MyLineMakRenderer myLineGutterRenderer){
        myLineGutterRenderer.showModifyDialog();
    }

    public static void showAddDialog(Editor editor,int editorLine){
        LineMarkDialog dialog = new LineMarkDialog(-1,-1);
        if(dialog.showAndGet()) {
            List<MyLineNumber> allMarkedLines = LineMarkManager.getAllMarkedLines(editor);

            MyLineNumber newLine = new MyLineNumber();
            newLine.setLineEditor(editorLine);
            newLine.setLabelIndex(dialog.getIndex());
            newLine.setLineSource(dialog.getSourceLine());
            allMarkedLines.add(newLine);

            LineMarkManager.markLineLighter(editor,allMarkedLines);
        }
    }

    public static void removeLineLabel(Editor editor,int editorLine) {
        List<MyLineNumber> allMarkedLines = LineMarkManager.getAllMarkedLines(editor);
        allMarkedLines.removeIf(myLine -> myLine.getLineEditor() == editorLine);
        LineMarkManager.markLineLighter(editor,allMarkedLines);
    }

}
