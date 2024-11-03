package com.liubs.vcb.ui;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.liubs.vcb.aggregate.MultiLineInstn;
import com.liubs.vcb.entity.MyInstructionInfo;
import com.liubs.vcb.entity.MyLineNumber;
import com.liubs.vcb.entity.Result;
import com.liubs.vcb.language.VCBLanguageFileType;
import com.liubs.vcb.tree.MethodTreeNode;
import com.liubs.vcb.constant.AccessConstant;
import com.liubs.vcb.ui.common.EditableLabel;
import com.liubs.vcb.util.ExceptionUtil;
import org.objectweb.asm.tree.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


/**
 * @author Liubsyy
 * @date 2024/10/21
 */
public class MethodPanel extends JPanel implements IPanelRefresh<MethodTreeNode>, Disposable {

    private Project project;
    private Editor editor;

    //access
    private EditableLabel access = new EditableLabel();

    //name
    private EditableLabel name = new EditableLabel();

    //desc
    private EditableLabel desc = new EditableLabel();

    //exception declared
    private EditableLabel exceptionDeclared = new EditableLabel();

    private JLabel maxStack = new JLabel();
    private JLabel maxLocals = new JLabel();

    //LineNumber
    private DefaultTableModel lineNumberTable;

    //LocalVariable
    private DefaultTableModel localVariableTable;

    //Exception Table
    private DefaultTableModel exceptionTable;


    //Params
    private DefaultTableModel paramsTable;

    private DefaultTableModel visibleAnnotations;
    private DefaultTableModel invisibleAnnotations;

    private Editor createEditor(){

        LightVirtualFile virtualFile = null;
        try{
            virtualFile = new LightVirtualFile("fileName.vcb", VCBLanguageFileType.INSTANCE, "");
            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            if(null != psiFile && null != psiFile.getVirtualFile()) {
                editor = EditorFactory.getInstance().createEditor(psiFile.getViewProvider().getDocument(), project);
                //关闭语法检测
                DaemonCodeAnalyzer daemonCodeAnalyzer = DaemonCodeAnalyzer.getInstance(project);
                daemonCodeAnalyzer.setHighlightingEnabled(psiFile, false);
            }
        }catch (Throwable ex){
            ex.printStackTrace();
        }
        if(null == editor) {
            Document document = EditorFactory.getInstance().createDocument("");
            editor = EditorFactory.getInstance().createEditor(document, project);
        }
        if(null != editor){
            if (editor instanceof EditorEx) {
                EditorEx editorEx = (EditorEx) editor;
                if(null != virtualFile) {
                    editorEx.setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project,virtualFile));
                }
                editorEx.setCaretVisible(true);
                editorEx.setEmbeddedIntoDialogWrapper(true);
            }
        }

        return editor;
    }

    public MethodPanel(Project project) {
        this.project = project;

        setLayout(new BorderLayout());

        this.editor = createEditor();

        JPanel baseInfo = FormBuilder.createFormBuilder()
                .setVerticalGap(8)
                .addLabeledComponent("Access : ", access)
                .addLabeledComponent("Name : ", name)
                .addLabeledComponent("Desc : ", desc)
                .addLabeledComponent("throws : ", exceptionDeclared)
                .getPanel();

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        baseInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(etchedBorder, "Method Info"),
                JBUI.Borders.empty(8)));


        exceptionTable = new DefaultTableModel(new Object[0][0],
                new String[]{"Start", "End", "Jump(Exception)","Type"});

        lineNumberTable = new DefaultTableModel(new Object[0][0],
                new String[]{"Label", "Line Number"}){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        localVariableTable = new DefaultTableModel(new Object[0][0],
                new String[]{"Index", "Start", "End","Name","Desc"});

        paramsTable = new DefaultTableModel(new Object[0][0], new String[]{"Name", "Access"}){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        visibleAnnotations = new DefaultTableModel(new Object[0][0], new String[]{"Desc", "Value"}){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invisibleAnnotations = new DefaultTableModel(new Object[0][0], new String[]{"Desc", "Value"}){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JPanel otherAttributePanel = FormBuilder.createFormBuilder()
                .setVerticalGap(8)
                .addLabeledComponent("Max Stack : ", maxStack)
                .addLabeledComponent("Max Locals : ", maxLocals)
                .addLabeledComponent("Parameters : ", new JBScrollPane(new JBTable(paramsTable)))
                .addLabeledComponent("Visible Annotations : ", new JBScrollPane(new JBTable(visibleAnnotations)))
                .addLabeledComponent("Invisible Annotations : ", new JBScrollPane(new JBTable(invisibleAnnotations)))
                .getPanel();

        //tabbed pane
        JBTabbedPane tabbedPane = new JBTabbedPane();
        tabbedPane.add("Code",editor.getComponent());
        tabbedPane.add("LocalVariable",new JBScrollPane(new JBTable(localVariableTable)));
        tabbedPane.add("Exception",new JBScrollPane(new JBTable(exceptionTable)));
        tabbedPane.add("LineNumber",new JBScrollPane(new JBTable(lineNumberTable)));
        tabbedPane.add("Others",otherAttributePanel);

        this.add(baseInfo,BorderLayout.NORTH);
        this.add(tabbedPane,BorderLayout.CENTER);

        initEditableAction();
    }


    private void initEditableAction(){
        access.onActionForCheckAccessBox("Method access", AccessConstant.METHOD_FLAGS,
                ()->methodNode.access,
                r-> {
                    methodNode.access = r;
                    refreshBaseInfo();
                });
        name.onActionForInput("Method name",null,r->{
            methodNode.name = r;
        });
        desc.onActionForInput("Method desc",null,r->{
            methodNode.desc = r;
        });
        exceptionDeclared.onActionForInput("Method throws",null,r->{
            methodNode.exceptions = Arrays.stream(r.trim().split(",")).collect(Collectors.toList());
        });
//        maxStack.onActionForInput("Max stack", IntInputValidator.INSTANCE, r->{
//            methodNode.maxStack = Integer.parseInt(r.trim());
//        });
//        maxLocals.onActionForInput("Max locals", IntInputValidator.INSTANCE, r->{
//            methodNode.maxLocals = Integer.parseInt(r.trim());
//        });
    }


    public void writeEditorContent(String content){
        Document document = editor.getDocument();

        // 使用 WriteCommandAction 修改文件内容
        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.setText(content);
        });

        // 提交文档更改
        PsiDocumentManager.getInstance(project).commitDocument(document);
    }

    private MethodNode methodNode;

    private void refreshBaseInfo(){
        if(methodNode==null){
            return;
        }
        access.setText(String.format("0x%04x(%s)", methodNode.access, String.join(" ",AccessConstant.getMethodFlagNames(methodNode.access))));
        name.setText(methodNode.name);
        desc.setText(methodNode.desc);
        exceptionDeclared.setText(String.join(",",methodNode.exceptions));
        maxStack.setText(String.valueOf(methodNode.maxStack));
        maxLocals.setText(String.valueOf(methodNode.maxLocals));
    }

    @Override
    public void refresh(MethodTreeNode treeNode) {

        methodNode = treeNode.getMethodNode();

        // 刷新基础信息和杂项信息
        refreshBaseInfo();

        //获取code指令信息
        MyInstructionInfo instructionInfo = treeNode.getInstructionInfo();

        //将字节码指令写入到编辑器
        writeEditorContent(instructionInfo.getAssemblyCode());
        Map<LabelNode, Integer> labelIndexMap = instructionInfo.getLabelIndexMap();

        //编辑器写入行号
        List<MyLineNumber> markLines = instructionInfo.getMarkLines();

        //行号标示高亮
        MyLineGutterRenderer.markLineLighter(editor,markLines);

        //行号表
        markLines = markLines.stream().filter(c->c.getLineSource()>=0).collect(Collectors.toList());
        fillTables(lineNumberTable,markLines,(n,dataRow)-> {
            dataRow[0] = "L"+n.getLabelIndex();
            dataRow[1] = n.getLineSource();
        });


        // 刷新LocalVariable
        fillTables(localVariableTable,methodNode.localVariables,(n,dataRow)-> {
            Integer start = labelIndexMap.get(n.start);
            Integer end = labelIndexMap.get(n.end);

            dataRow[0] = n.index;
            dataRow[1] = "L"+start;
            dataRow[2] = "L"+end;
            dataRow[3] = n.name;
            dataRow[4] = n.desc;
        });

        // 刷新Exception Table
        //TODO TryCatchBlockNode还有visibleTypeAnnotations和invisibleTypeAnnotations
        fillTables(exceptionTable,methodNode.tryCatchBlocks,(n,dataRow)-> {
            Integer start = labelIndexMap.get(n.start);
            Integer end = labelIndexMap.get(n.end);
            Integer handler = labelIndexMap.get(n.handler);

            dataRow[0] = "L"+start;
            dataRow[1] = "L"+end;
            dataRow[2] = "L"+handler;
            dataRow[3] = n.type;
        });


        // 刷新Parameters
        fillTables(paramsTable,methodNode.parameters,(n,dataRow)-> {
            dataRow[0] = n.name;
            dataRow[1] = n.access;
        });

        //刷新 visibleAnnotations
        fillTables(visibleAnnotations, methodNode.visibleAnnotations,(n,dataRow)->{
            dataRow[0] = n.desc;
            dataRow[1] = null == n.values ? null : n.values.stream().map(Object::toString).collect(Collectors.joining(","));
        });
        fillTables(invisibleAnnotations, methodNode.invisibleAnnotations,(n,dataRow)->{
            dataRow[0] = n.desc;
            dataRow[1] = null == n.values ? null : n.values.stream().map(Object::toString).collect(Collectors.joining(","));
        });

    }
    
    
    public static <T> void fillTables(DefaultTableModel tableModel, List<T> nodes, BiConsumer<T,Object[]> fillDataHandler) {
        if(null == nodes) {
            tableModel.setRowCount(0);
            return;
        }
        Object[][] newTableData = new Object[nodes.size()][tableModel.getColumnCount()];
        for(int i = 0,len=nodes.size() ; i<len ; i++){
            T n = nodes.get(i);
            fillDataHandler.accept(n,newTableData[i]);
        }
        tableModel.setRowCount(0);
        for (Object[] row : newTableData) {
            tableModel.addRow(row);
        }
    }


    @Override
    public void dispose() {
        try{
            EditorFactory.getInstance().releaseEditor(editor);
        }catch (Throwable e) {}

    }

    /**
     * 根据editor构建指令集合
     * @param treeNode
     * @return
     */
    @Override
    public Result<Void> savePanel(MethodTreeNode treeNode) {
        Result<Void> result = new Result<>();

        try{
            //获取行号
            List<MyLineNumber> allMarkedLines = MyLineGutterRenderer.getAllMarkedLines(editor);
            Map<Integer, MyLineNumber> lineEditorMap = allMarkedLines.stream()
                    .collect(Collectors.toMap(MyLineNumber::getLineEditor, myLineNumber -> myLineNumber));


            String assemblyCode = editor.getDocument().getText();
            String[] assemblyCodes = assemblyCode.split("\n");


            List<AbstractInsnNode> insnList = new ArrayList<>();

            //先构建LabelNode
            Map<String,LabelNode> labelNodeMap = new HashMap<>();
            for(MyLineNumber myLineNumber : allMarkedLines) {
                LabelNode labelNode = new LabelNode();
                labelNodeMap.put("L"+myLineNumber.getLabelIndex(),labelNode);
            }

            //构建局部变量
            List<LocalVariableNode> localVariables = new ArrayList<>();
            for (int row = 0; row < localVariableTable.getRowCount(); row++) {
                LabelNode start = labelNodeMap.get(localVariableTable.getValueAt(row, 1).toString());
                LabelNode end = labelNodeMap.get(localVariableTable.getValueAt(row, 2).toString());
                String name = localVariableTable.getValueAt(row, 3).toString();
                String desc = localVariableTable.getValueAt(row, 4).toString();
                LocalVariableNode localVariableNode = new LocalVariableNode(name,desc,null,start,end,row);

                localVariables.add(localVariableNode);
            }
            treeNode.getMethodNode().localVariables = localVariables;

            //构建指令
            MultiLineInstn multiLineInstn = null;
            for(int i=0;i < assemblyCodes.length ;i++) {

                //指令
                String line = assemblyCodes[i];
                if(line.trim().equals("")){
                    continue;
                }
                String[] split = line.split("\\s");
                String firstInstnType = split.length >0 ? split[0].trim().toLowerCase() : "";

                //多行指令结束
                if(null != multiLineInstn && multiLineInstn.isMultiEnd(firstInstnType)) {
                    AbstractInsnNode insnNode = multiLineInstn.parseInstnNode(labelNodeMap);
                    insnList.add(insnNode);
                    multiLineInstn = null;
                }

                //label和行号
                MyLineNumber myLineNumber = lineEditorMap.get(i);
                if(null != myLineNumber) {
                    LabelNode labelNode = labelNodeMap.get("L"+myLineNumber.getLabelIndex());
                    insnList.add(labelNode);
                    if(myLineNumber.getLineSource() > 0) {
                        insnList.add(new LineNumberNode(myLineNumber.getLineSource(),labelNode));
                    }
                }

                if(null == multiLineInstn) {
                    if(MultiLineInstn.isMultiLineInstn(firstInstnType)) {
                        //多行指令构建
                        multiLineInstn = MultiLineInstn.newInstn();
                        multiLineInstn.addLine(line);
                    }else {
                        String[] args = split.length>1 ? Arrays.copyOfRange(split, 1, split.length) : null;
                        AbstractInsnNode insnNode = treeNode.getAssemblyMethod().parseInstNode(labelNodeMap,firstInstnType, args);
                        insnList.add(insnNode);
                    }
                }else {
                    //多行指令构建
                    multiLineInstn.addLine(line);
                }
            }
            if(!allMarkedLines.isEmpty()
                    && assemblyCodes.length-1 < allMarkedLines.get(allMarkedLines.size()-1).getLineEditor()) {
                insnList.add( labelNodeMap.get("L"+ allMarkedLines.get(allMarkedLines.size()-1).getLabelIndex()));
            }

            InsnList instNodes = new InsnList();
            insnList.forEach(instNodes::add);

            treeNode.getMethodNode().instructions = instNodes;
            result.setSuccess(true);
        }catch (Exception ex) {
            result.setSuccess(false);
//            result.setErrorMessage(ExceptionUtil.getExceptionTracing(ex));
            result.setErrorMessage(ExceptionUtil.getExceptionTracing(ex));
        }

        return result;
    }
}
