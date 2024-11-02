package com.liubs.vcb.language;

import com.intellij.psi.tree.TokenSet;
import com.liubs.vcb.language.psi.VCBTypes;

/**
 * @author Liubsyy
 * @date 2024/10/29
 */
public interface MyTokenSets {

    TokenSet IDENTIFIERS = TokenSet.create(VCBTypes.KEY);

    TokenSet COMMENTS = TokenSet.create(VCBTypes.COMMENT);

}