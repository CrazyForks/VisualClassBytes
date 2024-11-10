package com.liubs.vcb.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.liubs.vcb.entity.Result;
import com.liubs.vcb.tree.*;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 右边的内容模块，动态展示字段/函数/接口等模块
 * @author Liubsyy
 * @date 2024/10/21
 */
public class ContentPanel extends JPanel {

    private ClassEditorPanel editorPanel;
    private Map<Class<?>,JPanel> panels = new HashMap<>();

    public ContentPanel(Project project,ClassEditorPanel editorPanel) {
        this.editorPanel = editorPanel;

        setLayout(new BorderLayout());

        ClassInfoPanel classInfoPanel = new ClassInfoPanel(project);
        MethodPanel methodPanel = new MethodPanel(project);
        FieldPanel fieldPanel = new FieldPanel(project);
        InnerClassPanel innerClassInfoPanel = new InnerClassPanel(project);
        ConstPoolPanel constPoolPanel = new ConstPoolPanel(editorPanel,project);

        panels.put(ClassInfoTreeNode.class, classInfoPanel);
        panels.put(ConstantTreeNode.class, constPoolPanel);
        panels.put(FieldTreeNode.class, fieldPanel);
        panels.put(MethodTreeNode.class, methodPanel);
        panels.put(InnerClassTreeNode.class, innerClassInfoPanel);

    }

    private JPanel currentPanel = null;

    public void refresh(BaseTreeNode selectedNode){

        JPanel panel = panels.get(selectedNode.getClass());
        if(null != currentPanel
                        && (currentPanel instanceof ConstPoolPanel)
                        && (panel instanceof ConstPoolPanel)){
            ((ConstPoolPanel) currentPanel).refresh((ConstantTreeNode) selectedNode);
            return;
        }

        this.removeAll();
        if(null != panel) {
            if(panel instanceof IPanelRefresh) {
                IPanelRefresh panelRefresh = (IPanelRefresh)panel;
                panelRefresh.refresh(selectedNode);
                this.add(panel);
                currentPanel = panel;
            }
        }
        this.revalidate();
        this.repaint();
    }

    public Result<Void> saveContent(BaseTreeNode selectedNode){
        JPanel panel = panels.get(selectedNode.getClass());
        if(null != panel) {
            if(panel instanceof IPanelRefresh) {
                IPanelRefresh panelRefresh = (IPanelRefresh)panel;
                return panelRefresh.savePanel(selectedNode);
            }
        }
        return Result.success();
    }


    public void dispose() {
        panels.values().forEach(c->{
            if(c instanceof Disposable) {
                ((Disposable)c).dispose();
            }
        });
    }






}
