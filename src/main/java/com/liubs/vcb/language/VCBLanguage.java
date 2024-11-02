package com.liubs.vcb.language;

import com.intellij.lang.Language;

/**
 * @author Liubsyy
 * @date 2024/10/28
 */
public class VCBLanguage extends Language {
    public static final VCBLanguage INSTANCE = new VCBLanguage();

    private VCBLanguage() {
        super("liubsyy_VCB");
    }

}
