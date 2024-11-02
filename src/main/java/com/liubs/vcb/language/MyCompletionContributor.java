package com.liubs.vcb.language;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.liubs.vcb.constant.InstructionConstant;
import com.liubs.vcb.language.psi.VCBTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/28
 * 代码自动补全
 */
public class MyCompletionContributor extends CompletionContributor {
    public MyCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(VCBTypes.KEY_OTHER),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        //关键字自动补全
                        for (String opCode : InstructionConstant.OPCODES) {
                            result.addElement(LookupElementBuilder.create(opCode));
                        }
                    }
                });
    }
}
