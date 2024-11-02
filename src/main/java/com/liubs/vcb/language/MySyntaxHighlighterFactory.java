package com.liubs.vcb.language;

import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/29
 */
final class MySyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    @NotNull
    @Override
    public com.intellij.openapi.fileTypes.SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        return new MySyntaxHighlighter();
    }

}