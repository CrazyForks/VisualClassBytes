package com.liubs.vcb.language;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.liubs.vcb.constant.InstructionConstant;
import com.liubs.vcb.language.psi.VCBTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liubsyy
 * @date 2024/10/28
 * 代码自动补全
 */
public class MyCompletionContributor extends CompletionContributor {

    //关键字自动补全
    private static final List<LookupElement> LOOKUP_ELEMENTS = new ArrayList<>();
    static {
        for (String opCode : InstructionConstant.OPCODES) {
            LOOKUP_ELEMENTS.add(LookupElementBuilder.create(opCode));
        }
    }

    public MyCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(VCBTypes.KEY_OTHER),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        // 获取光标位置和文档
                        int offset = parameters.getOffset();
                        Document document = parameters.getEditor().getDocument();
                        int lineNumber = document.getLineNumber(offset);
                        int lineStartOffset = document.getLineStartOffset(lineNumber);

                        // 获取光标所在行，从行首到光标位置的文本
                        String textBeforeCaret = document.getText(new TextRange(lineStartOffset, offset));
                        String trimmedText = textBeforeCaret.trim();

                        if( !trimmedText.contains(" ") ) {    //只有每行第一个关键字需要代码补全，第二个就不是关键字了
                            result.addAllElements(LOOKUP_ELEMENTS);
                        }
                    }
                });
    }
}
