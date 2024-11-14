package com.liubs.vcb.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.SideBorder;
import com.intellij.ui.components.JBScrollPane;
import com.liubs.vcb.domain.JarSave;
import com.liubs.vcb.project.ProjectDependency;
import com.liubs.vcb.tree.ConstantTreeNode;
import com.liubs.vcb.ui.common.RadioDialog;
import com.liubs.vcb.util.NoticeInfo;
import com.liubs.vcb.domain.assemblycode.MyAssemblyClass;
import com.liubs.vcb.entity.Result;
import com.liubs.vcb.tree.BaseTreeNode;
import com.liubs.vcb.action.MyActionButton;

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
    private JBScrollPane treeScrollPane;

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
        myTree.initTree(asmClassService);
        myTree.addTreeSelectionListener(this);
        treeScrollPane = new JBScrollPane(myTree);

        //左边panel
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(treeScrollPane, BorderLayout.CENTER);
        leftPanel.add(this.createToolBar(),BorderLayout.SOUTH);

        //右边panel
        rightPanel = new ContentPanel(project,this);

        //分割线
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(260); // 初始分割线位置 (以像素为单位)
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

        //必须等rightPanel初始化完后再默认选中第一个节点
        myTree.selectNode((BaseTreeNode)myTree.getRootNode().getFirstChild());
    }


    private JPanel createToolBar(){
        JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBarPanel.setBorder(IdeBorderFactory.createBorder(SideBorder.LEFT|SideBorder.RIGHT|SideBorder.BOTTOM));

        ArrayList<AnAction> actions = new ArrayList<>();

        AnAction saveClassAction = new MyActionButton("Save class",AllIcons.Actions.MenuSaveall, this::saveClass);
//        AnAction dumpASMCodeAction = new MyActionButton("Dump ASM access code", AllIcons.Actions.Download, null);
        AnAction refreshAction = new MyActionButton("Refresh", AllIcons.Actions.Refresh, this::refreshTree);


        AnAction addField = new MyActionButton("Field",AllIcons.Nodes.Field, null);
        AnAction addMethod = new MyActionButton("Method",AllIcons.Nodes.Method, null);
        DefaultActionGroup addGroupWrapper = new DefaultActionGroup("Add", Arrays.asList(addField,addMethod));
        addGroupWrapper.setPopup(true);
        addGroupWrapper.getTemplatePresentation().setIcon(AllIcons.General.Add);

        actions.add(saveClassAction);
//        actions.add(addGroupWrapper);
//        actions.add(dumpASMCodeAction);
        actions.add(refreshAction);

        ActionToolbar myToolBar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR,
                new DefaultActionGroup(actions), false);
        myToolBar.setOrientation(SwingConstants.HORIZONTAL); //设置工具栏为水平方向

        toolBarPanel.add(myToolBar.getComponent());
        return toolBarPanel;
    }

    private void refreshTree(){

        virtualFile.refresh(false,false);
        try {
            byte[] classBytes = VfsUtilCore.loadBytes(virtualFile);
            asmClassService = new MyAssemblyClass(classBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String selectPath = null;
        BaseTreeNode selectedNode = (BaseTreeNode) myTree.getLastSelectedPathComponent();
        if(selectedNode != null) {
            selectPath = Arrays.toString(selectedNode.getPath());
        }

        //创建一个新的tree，refreshTree总有东西没有摘干净
        myTree = new MyTree();
        myTree.initTree(asmClassService);
        myTree.addTreeSelectionListener(this);

        boolean selectAny = false;
        if(null != selectPath) {
            for(BaseTreeNode c : myTree.allNodes()) {
                if(selectPath.equals(Arrays.toString(c.getPath()))){
                    selectAny = true;
                    myTree.selectNode(c);
                    break;
                }
            }
        }

        if(!selectAny){
            myTree.selectNode((BaseTreeNode)myTree.getRootNode().getFirstChild());
        }

        treeScrollPane.setViewportView(myTree);
    }

    private void saveClass(){

        if(myTree.getLastSelectedPathComponent() instanceof ConstantTreeNode) {
            saveConstPool();
            return;
        }

        //先保存panel内存信息
        BaseTreeNode selectedNode = (BaseTreeNode)myTree.getLastSelectedPathComponent();
        Result<Void> saveContentResult = rightPanel.saveContent(selectedNode);
        if(!saveContentResult.isSuccess()) {
            NoticeInfo.error("Save fail:"+saveContentResult.getErrorMessage());
            return;
        }

        //保存字节码
        Result<byte[]> result = asmClassService.dumpBytes(ProjectDependency.getDependentLibURLs(project));
        if(!result.isSuccess()){
            NoticeInfo.error("Save fail:"+result.getErrorMessage());
            return;
        }

        saveClassFile(result.getData());
    }

    public void saveConstPool(){
        Result<byte[]> result = asmClassService.saveConstPoolToClass();
        if(!result.isSuccess()){
            NoticeInfo.error("Save fail:"+result.getErrorMessage());
            return;
        }
        saveClassFile(result.getData());
    }


    private void saveClassFile(byte[] bytes){
        try {
            if(virtualFile.getPath().contains(".jar!")){
                RadioDialog radioDialog = new RadioDialog("Save class in jar",
                        null,
                        Arrays.asList("Save to temp directory","Update jar"),new Dimension(180,50));
                if(radioDialog.showAndGet()){
                    boolean updateJar = radioDialog.getSelectRadio() == 1;
                    JarSave jarSave = new JarSave(virtualFile);
                    Result<String> r = updateJar ?
                            jarSave.updateJar(bytes) : jarSave.saveTemp(bytes);
                    VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                    virtualFile.refresh(false,false);
                    ApplicationManager.getApplication().invokeLater(() -> {
                        if(r.isSuccess()) {
                            NoticeInfo.info("Save success to "+r.getData());
                        }else {
                            NoticeInfo.error("Save err:"+r.getErrorMessage());
                        }
                    });
                }
            }else {
                Files.write(Paths.get(virtualFile.getPath()),bytes);
                NoticeInfo.info("Save success to "+virtualFile.getPath());
                VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                virtualFile.refresh(false,false);
            }
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

    public MyTree getMyTree() {
        return myTree;
    }
}
