package com.liubs.vcb.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.liubs.vcb.ui.ClassEditorPanel;
import com.liubs.vcb.util.NoticeInfo;
import com.liubs.vcb.editor.VisualClassEditor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Liubsyy
 * @date 2024/10/26
 */
public abstract class ClassEditorActionBase extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile selectedFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        Project project = e.getProject();
        if(project == null) {
            NoticeInfo.warning("Please open a project");
            return;
        }
        if(null == selectedFile) {
            NoticeInfo.warning("No file selected");
            return;
        }
        if(!"class".equals(selectedFile.getExtension())){
            NoticeInfo.warning("Visual ClassBytes only work in class file");
            return;
        }

        performAction(e,selectedFile);
    }

    protected abstract void performAction(AnActionEvent e,VirtualFile selectedFile);


    public ClassEditorPanel getEditorPanel(Project project, VirtualFile selectedFile){
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        FileEditor[] fileEditors = fileEditorManager.openFile(selectedFile, true);
        for (FileEditor fileEditor : fileEditors) {
            if (!(fileEditor instanceof VisualClassEditor)) {
                continue;
            }
            VisualClassEditor visualClassEditor = (VisualClassEditor)fileEditor;
            return visualClassEditor.getMainPanel();
        }
        return null;
    }
}
