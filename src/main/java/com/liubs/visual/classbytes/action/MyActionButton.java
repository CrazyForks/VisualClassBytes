package com.liubs.visual.classbytes.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Liubsyy
 * @date 2024/10/26
 */
public class MyActionButton extends AnAction {

    private Action action;
    public MyActionButton(String text, Icon icon,Action action){
        super(text,text,icon);
        this.action = action;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        action.execute();
    }

    @FunctionalInterface
    public interface Action {
        void execute();
    }
}
