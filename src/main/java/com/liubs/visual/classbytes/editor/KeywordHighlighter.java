package com.liubs.visual.classbytes.editor;


import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.*;
import groovyjarjarasm.asm.util.Printer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liubsyy
 * @date 2024/10/27
 */

public class KeywordHighlighter {
    private final Editor editor;

    private static final Map<String, Pattern> KEY_WORDS_PATTERNS = new HashMap<>();


    static {
        for(String w : Printer.OPCODES) {
            KEY_WORDS_PATTERNS.put(w, Pattern.compile("\\b" + Pattern.quote(w) + "\\b"));
            KEY_WORDS_PATTERNS.put(w.toLowerCase(), Pattern.compile("\\b" + Pattern.quote(w.toLowerCase()) + "\\b"));
        }
    }

    public KeywordHighlighter(Editor editor) {
        this.editor = editor;
    }


    public void autoHighlighter() {
        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                highlightKeywords();
            }
        });
        highlightKeywords(); // 初始化时也高亮一次
    }

    private void highlightKeywords() {
        MarkupModel markupModel = editor.getMarkupModel();
        markupModel.removeAllHighlighters(); // 清除现有的高亮

        String text = editor.getDocument().getText();

        TextAttributes keywordAttributes = EditorColorsManager.getInstance()
                .getGlobalScheme().getAttributes(DefaultLanguageHighlighterColors.KEYWORD);

        for (Pattern pattern : KEY_WORDS_PATTERNS.values()) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                markupModel.addRangeHighlighter(
                        start,
                        end,
                        HighlighterLayer.ADDITIONAL_SYNTAX,
                        keywordAttributes,
                        HighlighterTargetArea.EXACT_RANGE
                );
            }
        }
    }
}
