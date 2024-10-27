package com.liubs.visual.classbytes.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.liubs.visual.classbytes.bean.OpenedEditors;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class VisualClassProvider implements FileEditorProvider {
    public static final String EDITOR_TYPE_ID = "liubsyy-visual-class-bytes-editor";

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return "class".equalsIgnoreCase(file.getExtension())
                && OpenedEditors.getInstance(project).containsPath(file.getPath());
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return new VisualClassEditor(project,file);
    }


    @NotNull
    @Override
    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    @Override
    public @NotNull
    FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }



}
