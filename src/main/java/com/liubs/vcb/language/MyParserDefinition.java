package com.liubs.vcb.language;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.liubs.vcb.language.parser.VCBParser;
import com.liubs.vcb.language.psi.VCBTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/29
 */
final class MyParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(VCBLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new LexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return MyTokenSets.COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new VCBParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new MyPsiFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return VCBTypes.Factory.createElement(node);
    }

}