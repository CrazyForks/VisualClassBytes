package com.liubs.vcb.language;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/29
 */
public class MyPsiFile extends PsiFileBase {

    public MyPsiFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, VCBLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return VCBLanguageFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "vcb File";
    }

}