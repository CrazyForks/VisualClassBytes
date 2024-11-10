package com.liubs.vcb.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.liubs.vcb.action.MyActionButton;
import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.tree.ConstantTreeNode;
import com.liubs.vcb.ui.constpool.*;
import org.apache.bcel.Const;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class ConstPoolPanel extends JPanel implements IPanelRefresh<ConstantTreeNode> {
    private Project project;
    private Map<Byte, ConstBasePanel> constBasePanelMap = new HashMap<>();

    private JPanel constPoolInfo;

    private int stepVisit=-1; //当前下标
    private ArrayList<Integer> steps = new ArrayList<>();  //所有访问记录

    private ClassEditorPanel editorPanel;
    public ConstPoolPanel(ClassEditorPanel editorPanel,Project project) {
        this.editorPanel = editorPanel;
        this.project = project;

        setLayout(new BorderLayout());

        constPoolInfo = new JPanel(new BorderLayout());
        this.add(constPoolInfo,BorderLayout.NORTH);
        this.add(this.createToolBar(),BorderLayout.CENTER);


        constBasePanelMap.put(Const.CONSTANT_Utf8,new Utf8ConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Integer,new IntegerConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Float,new FloatConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Long,new LongConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Double,new DoubleConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Class,new ClassConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Fieldref,new FieldRefConstPanel());
        constBasePanelMap.put(Const.CONSTANT_String,new StringConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Methodref,new MethodRefConstPanel());
        constBasePanelMap.put(Const.CONSTANT_InterfaceMethodref,new InterfaceMethodrefConstPanel());
        constBasePanelMap.put(Const.CONSTANT_NameAndType,new NameAndTypeConstPanel());
        constBasePanelMap.put(Const.CONSTANT_MethodHandle,new MethodHandleConstPanel());
        constBasePanelMap.put(Const.CONSTANT_MethodType,new MethodTypeConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Dynamic,new DynamicConstPanel());
        constBasePanelMap.put(Const.CONSTANT_InvokeDynamic,new InvokeDynamicConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Module,new ModuleConstPanel());
        constBasePanelMap.put(Const.CONSTANT_Package,new PackageConstPanel());

        constBasePanelMap.forEach((k,v)-> {
            v.setEditorPanel(editorPanel);
            v.setConstPoolPanel(ConstPoolPanel.this);
        });

    }

    public JPanel createToolBar(){
        JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ArrayList<AnAction> actions = new ArrayList<>();

        AnAction back = new MyActionButton("Back", AllIcons.Actions.Back, this::back);
        AnAction forward = new MyActionButton("Forward", AllIcons.Actions.Forward, this::forward);
        AnAction saveClassAction = new MyActionButton("Save constant pool", AllIcons.Actions.MenuSaveall, this::saveClass);

        actions.add(back);
        actions.add(forward);
        actions.add(saveClassAction);

        ActionToolbar myToolBar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, new DefaultActionGroup(actions), false);
        myToolBar.setOrientation(SwingConstants.HORIZONTAL); //设置工具栏为水平方向

        toolBarPanel.add(myToolBar.getComponent());

        return toolBarPanel;
    }

    public void putStep(int step){
        if(stepVisit>=0 && step == steps.get(stepVisit)) {
            return;
        }

        if(stepVisit >0 && stepVisit < steps.size() -1) {
            for(int i = steps.size()-1 ; i > stepVisit; i--){
                steps.remove(i);
            }
        }
        if(steps.size()>=100 && stepVisit>0){
            steps.remove(0);
            stepVisit--;
        }

        stepVisit++;
        steps.add(step);
    }

    private void back(){
        if(stepVisit > 0){
            stepVisit--;
            Integer index = steps.get(stepVisit);
            MyTree myTree = editorPanel.getMyTree();
            ConstantTreeNode constantTreeNode = myTree.getConstantTreeNodeMap().get(index);
            if(null != constantTreeNode) {
                myTree.selectNode(constantTreeNode);
            }
        }

    }
    private void forward(){
        if(stepVisit<steps.size()-1) {
            stepVisit++;
            Integer index = steps.get(stepVisit);
            MyTree myTree = editorPanel.getMyTree();
            ConstantTreeNode constantTreeNode = myTree.getConstantTreeNodeMap().get(index);
            if(null != constantTreeNode) {
                myTree.selectNode(constantTreeNode);
            }
        }
    }

    private void saveClass(){
        editorPanel.saveConstPool();
    }


    @Override
    public void refresh(ConstantTreeNode treeNode) {
        MyAssemblyConst myAssemblyConst = treeNode.getMyAssemblyConst();
        if(null != myAssemblyConst) {
            constPoolInfo.removeAll();
            ConstBasePanel constBasePanel = constBasePanelMap.get(myAssemblyConst.getTag());
            if(null != constBasePanel) {
                JPanel panel = constBasePanel.getPanel();
                constPoolInfo.add(panel,BorderLayout.NORTH);
                constBasePanel.refresh(myAssemblyConst);

                putStep(myAssemblyConst.getIndex());
            }
            constPoolInfo.revalidate();
            constPoolInfo.repaint();
        }
    }
}
