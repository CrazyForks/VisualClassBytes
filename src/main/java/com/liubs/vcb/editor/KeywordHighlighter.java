package com.liubs.vcb.editor;


import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.util.Key;
import groovyjarjarasm.asm.util.Printer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Liubsyy
 * @date 2024/10/27
 */

public class KeywordHighlighter {
    private static final Key<Boolean> KEYWORDHIGHLIGHTER = Key.create("liubsyy_KeywordHighlighter");

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

        // 移除旧的 Highlighter
        List<RangeHighlighter> toRemove = new ArrayList<>();
        for (RangeHighlighter highlighter : markupModel.getAllHighlighters()) {
            if (Boolean.TRUE.equals(highlighter.getUserData(KEYWORDHIGHLIGHTER))) {
                toRemove.add(highlighter);
            }
        }
        for (RangeHighlighter highlighter : toRemove) {
            markupModel.removeHighlighter(highlighter);
        }


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
