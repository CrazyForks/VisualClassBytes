package com.liubs.visual.classbytes.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.structuralsearch.plugin.ui.Spacer;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.SideBorder;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.liubs.visual.classbytes.aggregate.MyAssemblyClass;
import com.liubs.visual.classbytes.entity.Result;
import com.liubs.visual.classbytes.tree.BaseTreeNode;
import com.liubs.visual.classbytes.action.MyActionButton;
import com.liubs.visual.classbytes.util.NoticeInfo;
import icons.MyIcons;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Liubsyy
 * @date 2024/10/19
 */
public class ClassEditorPanel extends JPanel implements TreeSelectionListener{

    private Project project;
    private VirtualFile virtualFile;

    private MyAssemblyClass asmClassService;

    private MyTree myTree;

    private JPanel leftPanel;
    private ContentPanel rightPanel;

    public ClassEditorPanel(Project project, VirtualFile virtualFile){
        this.project = project;
        this.virtualFile = virtualFile;

        try {
            byte[] classBytes = VfsUtilCore.loadBytes(virtualFile);
            asmClassService = new MyAssemblyClass(classBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setLayout(new BorderLayout());


        // 创建树
        myTree = new MyTree();
        BaseTreeNode rootNode = myTree.initNodes(asmClassService);
        myTree.addTreeSelectionListener(this);
        JBScrollPane treeScrollPane = new JBScrollPane(myTree);

        //左边panel
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        leftPanel.add(this.createToolBar(),BorderLayout.SOUTH);

        //右边panel
        rightPanel = new ContentPanel(project);

        //分割线
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300); // 初始分割线位置 (以像素为单位)
        splitPane.setResizeWeight(0.3); // 初始树面板占 30% 的宽度
//        splitPane.setOneTouchExpandable(true); // 在分割线两边增加可展开/折叠按钮
        splitPane.setDividerSize(1);
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(JBColor.LIGHT_GRAY); // 设置分割线的颜色
                        g.fillRect(0, 0, getWidth(), getHeight());
                        super.paint(g);
                    }
                };
            }
        });

        //限制树的最大宽度
        treeScrollPane.setMinimumSize(new Dimension(100, 0));  // 最小宽度 100px
        treeScrollPane.setMaximumSize(new Dimension(400, Integer.MAX_VALUE)); // 最大宽度 400px

        this.add(splitPane, BorderLayout.CENTER);

        //选中ClassInfo
        try{
            myTree.selectNode((BaseTreeNode)rootNode.getFirstChild());
        }catch (Throwable ex){}
    }


    private JPanel createToolBar(){
        JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBarPanel.setBorder(IdeBorderFactory.createBorder(SideBorder.LEFT|SideBorder.RIGHT|SideBorder.BOTTOM));

        ArrayList<AnAction> actions = new ArrayList<>();

        AnAction saveClassAction = new MyActionButton("Save class",AllIcons.Actions.MenuSaveall, this::saveClass);
        AnAction dumpASMCodeAction = new MyActionButton("Dump ASM access code", AllIcons.Actions.Download, null);
        AnAction refreshAction = new MyActionButton("Refresh", AllIcons.Actions.Refresh, null);


        AnAction addField = new MyActionButton("Field",AllIcons.Nodes.Field, null);
        AnAction addMethod = new MyActionButton("Method",AllIcons.Nodes.Method, null);
        DefaultActionGroup addGroupWrapper = new DefaultActionGroup("Add", Arrays.asList(addField,addMethod));
        addGroupWrapper.setPopup(true);
        addGroupWrapper.getTemplatePresentation().setIcon(AllIcons.General.Add);

        actions.add(saveClassAction);
        actions.add(addGroupWrapper);
        actions.add(dumpASMCodeAction);
        actions.add(refreshAction);

        ActionToolbar myToolBar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR,
                new DefaultActionGroup(actions), false);
        myToolBar.setOrientation(SwingConstants.HORIZONTAL); //设置工具栏为水平方向

        toolBarPanel.add(myToolBar.getComponent());
        return toolBarPanel;
    }

    private void saveClass(){

        //先保存panel内存信息
        BaseTreeNode selectedNode = (BaseTreeNode)myTree.getLastSelectedPathComponent();
        Result<Void> saveContentResult = rightPanel.saveContent(selectedNode);
        if(!saveContentResult.isSuccess()) {
            NoticeInfo.error("Save fail:"+saveContentResult.getErrorMessage());
            return;
        }

        //保存字节码
        Result<byte[]> result = asmClassService.dumpBytes();
        if(!result.isSuccess()){
            NoticeInfo.error("Save fail:"+result.getErrorMessage());
            return;
        }
        try {
            Files.write(Paths.get(virtualFile.getPath()),result.getData());
            NoticeInfo.info("Save success to "+virtualFile.getPath());

            virtualFile.refresh(false,false);
        } catch (IOException e) {
            e.printStackTrace();
            NoticeInfo.error("Save fail:"+e.getMessage());
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        // 获取选中的节点
        BaseTreeNode selectedNode = (BaseTreeNode) myTree.getLastSelectedPathComponent();
        if (selectedNode == null) return;

        // 获取选中的节点名称
        //String nodeName = selectedNode.getUserObject().toString();

        rightPanel.refresh(selectedNode);
    }



    public void dispose() {
        if(null != rightPanel) {
            rightPanel.dispose();
        }

    }
}
