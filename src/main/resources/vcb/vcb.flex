package com.liubs.vcb.language;

//  private boolean zzAtBOL,zzEOFDone;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.liubs.vcb.language.psi.VCBTypes;
import com.intellij.psi.TokenType;

%%

%class VCBLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
FIRST_VALUE_CHARACTER=[^ \n\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
SEPARATOR=[:=]
KEY_CHARACTER=[^:=\ \n\t\f\\] | "\\ "

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return VCBTypes.COMMENT; }

<YYINITIAL> {KEY_CHARACTER}+
     {
          String text = yytext().toString();
          if (com.liubs.vcb.constant.InstructionConstant.OPCODES.contains(text)) {
             yybegin(YYINITIAL);
             return VCBTypes.KEY;
          } else {
              yybegin(YYINITIAL);
              return VCBTypes.KEY_OTHER;
          }

      }

<YYINITIAL> {SEPARATOR}                                     { yybegin(WAITING_VALUE); return VCBTypes.SEPARATOR; }

<WAITING_VALUE> {CRLF}({CRLF}|{WHITE_SPACE})+               { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return VCBTypes.VALUE; }

({CRLF}|{WHITE_SPACE})+                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^]                                                         { return TokenType.BAD_CHARACTER; }