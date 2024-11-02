package com.liubs.vcb.language;

import com.intellij.lexer.FlexAdapter;

/**
 * @author Liubsyy
 * @date 2024/10/29
 */
public class LexerAdapter extends FlexAdapter {

    public LexerAdapter() {
        super(new VCBLexer(null));
    }

}
