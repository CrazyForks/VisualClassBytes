// This is a generated file. Not intended for manual editing.
package com.liubs.vcb.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.liubs.vcb.language.VCBElementType;
import com.liubs.vcb.language.VCBTokenType;
import com.liubs.vcb.language.psi.impl.*;

public interface VCBTypes {

  IElementType PROPERTY = new VCBElementType("PROPERTY");

  IElementType COMMENT = new VCBTokenType("COMMENT");
  IElementType CRLF = new VCBTokenType("CRLF");
  IElementType KEY = new VCBTokenType("KEY");
  IElementType KEY_OTHER = new VCBTokenType("KEY_OTHER");
  IElementType SEPARATOR = new VCBTokenType("SEPARATOR");
  IElementType VALUE = new VCBTokenType("VALUE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == PROPERTY) {
        return new VCBPropertyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
