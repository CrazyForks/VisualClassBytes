package com.liubs.vcb.language;

/**
 * @author Liubsyy
 * @date 2024/10/28
 */
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VCBLanguageFileType extends LanguageFileType {
    public static final VCBLanguageFileType INSTANCE = new VCBLanguageFileType();

    private VCBLanguageFileType() {
        super(VCBLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "VCB File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "VCB custom file type";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "vcb";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.JavaClass;
    }
}
