package com.liubs.vcb.language;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/29
 */
public class VCBTokenType extends IElementType {

    public VCBTokenType(@NotNull @NonNls String debugName) {
        super(debugName, VCBLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "VCBTokenType." + super.toString();
    }

}