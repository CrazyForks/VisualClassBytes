package com.liubs.vcb.language;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/29
 */
public class VCBElementType extends IElementType {

    public VCBElementType(@NotNull @NonNls String debugName) {
        super(debugName, VCBLanguage.INSTANCE);
    }

}