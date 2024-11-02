package com.liubs.vcb.editor;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBList;
import groovyjarjarasm.asm.util.Printer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liubsyy
 * @date 2024/10/27
 */
public class AutoComplete {

    private final Editor editor;
    private JBPopup currentPopup;
    private static final List<String> KEY_WORDS = new ArrayList<>();

    static {
        for (String w : Printer.OPCODES) {
            KEY_WORDS.add(w);
            KEY_WORDS.add(w.toLowerCase());
        }
    }

    public AutoComplete(Editor editor) {
        this.editor = editor;
    }

    public void autoComplete() {
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                String wordAtCaret = getWordAtCaret(event.getDocument(), event.getOffset());
                if (wordAtCaret == null || wordAtCaret.isEmpty()) {
                    if (currentPopup != null && currentPopup.isVisible()) {
                        currentPopup.cancel();
                    }
                    return;
                }
                showPopup(wordAtCaret, event);
            }
        });


    }

    private void showPopup(String wordAtCaret, DocumentEvent event) {
        List<String> resultKeys = KEY_WORDS.stream()
                .filter(c -> c.startsWith(wordAtCaret))
                .collect(Collectors.toList());

        if (resultKeys.isEmpty()) {
            if (currentPopup != null && currentPopup.isVisible()) {
                currentPopup.cancel();
            }
            return;
        }

        JBList<String> list = new JBList<>(resultKeys);

        if (currentPopup != null && currentPopup.isVisible()) {
            currentPopup.cancel();
        }

        currentPopup = new PopupChooserBuilder<>(list)
                .setRequestFocus(false) // 弹窗不抢焦点，继续在编辑器中输入
                .setItemChoosenCallback(() -> {
                    String selected = list.getSelectedValue();
                    if (selected != null) {
                        int startOffset = event.getOffset() - wordAtCaret.length()+1;
                        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
                            event.getDocument().replaceString(startOffset, event.getOffset()+1, selected);
                        });
                        editor.getCaretModel().moveToOffset(startOffset + selected.length());
                        if (currentPopup != null && currentPopup.isVisible()) {
                            currentPopup.cancel();
                        }
                    }
                })
                .createPopup();

        currentPopup.showInBestPositionFor(editor);
    }

    private static String getWordAtCaret(Document document, int offset) {
        if (offset < 0 || offset >= document.getTextLength()) {
            return null;
        }

        String text = document.getText();
        int start = offset;
        int end = offset;

        while (start > 0 && Character.isLetterOrDigit(text.charAt(start - 1))) {
            start--;
        }
        while (end < text.length() && Character.isLetterOrDigit(text.charAt(end))) {
            end++;
        }

        if (start < end) {
            return text.substring(start, end);
        }
        return null;
    }
}
