package com.liubs.vcb.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.liubs.vcb.bean.OpenedEditors;
import com.liubs.vcb.editor.VisualClassProvider;

/**
 * @author Liubsyy
 * @date 2024/10/26
 */
public class OpenClassEditorAction extends ClassEditorActionBase {

    @Override
    protected void performAction(AnActionEvent e, VirtualFile selectedFile) {
        FileEditorManager editorManager = FileEditorManager.getInstance(e.getProject());
        FileEditor[] editors = editorManager.getEditors(selectedFile);

        // 关闭现有的编辑器
        if (editors.length > 0) {
            editorManager.closeFile(selectedFile);
        }
        OpenedEditors.getInstance(e.getProject()).addPath(selectedFile.getPath());

        // 重新打开文件以触发编辑器的创建
        editorManager.openFile(selectedFile, true);
        editorManager.setSelectedEditor(selectedFile, VisualClassProvider.EDITOR_TYPE_ID);
    }
}
