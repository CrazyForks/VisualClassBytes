package vcb.checkclass;

import com.liubs.vcb.domain.assemblycode.MyAssemblyClass;
import com.liubs.vcb.domain.assemblycode.MyAssemblyMethod;
import com.liubs.vcb.domain.assemblycode.MyClassWriter;
import com.liubs.vcb.domain.instn.MultiLineInstn;
import com.liubs.vcb.entity.MyInstructionInfo;
import com.liubs.vcb.entity.MyLineNumber;
import com.liubs.vcb.util.ExceptionUtil;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * 自动跑任务，将类的所有函数输出字节码，然后将字节码保存成class,校验生成字节码是否异常
 * @author Liubsyy
 * @date 2024/11/9
 */
public class AutoCheckMethod {


    /**
     * 测试jar内所有class字节码
     *
     * @throws Exception
     */
    @Test
    public void testJar() throws Exception {
//        String jarPath = "/Users/liubs/.m2/repository/ch/qos/logback/logback-classic/1.2.12/logback-classic-1.2.12.jar";
//        String jarPath = "/Users/liubs/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.13.5/jackson-core-2.13.5.jar";
//        String jarPath = "/Users/liubs/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.13.5/jackson-databind-2.13.5.jar";
//        String jarPath = "/Users/liubs/.m2/repository/com/google/code/gson/gson/2.8.3/gson-2.8.3.jar";
        String jarPath = "/Users/liubs/.m2/repository/org/springframework/spring-core/5.3.27/spring-core-5.3.27.jar";

        List<URL> urls = Arrays.asList(new File(jarPath).toURI().toURL());
        try (JarFile jarFile = new JarFile(jarPath)){
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if(entry.getName().endsWith(".class")){

                    byte[] bytes;
                    try (InputStream inputStream = jarFile.getInputStream(entry);
                         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        bytes = outputStream.toByteArray();
                    }
                    checkClass(urls,bytes);
                }
            }
        }
    }

    //测试类的所有函数字节码是否正确
    @Test
    public void testAssemblyClass() throws IOException {
        testAssemblyClass("/Users/liubs/IdeaProjects/TestIDEA/target/classes/com/liubs/instn/TestLDC.class");
    }


    public void testAssemblyClass(String classFilePath) throws IOException {
        checkClass(null,Files.readAllBytes(Paths.get(classFilePath)));
    }


    private void checkClass(List<URL> dependentLib,byte[] classBytes){
        MyAssemblyClass asmClass = new MyAssemblyClass(classBytes);


        List<MethodNode> methods = asmClass.getClassNode().methods;
        for(MethodNode method : methods) {
            MyAssemblyMethod assemblyMethod = new MyAssemblyMethod(method);
            try{
                checkMethod(dependentLib,asmClass,assemblyMethod);

                System.out.printf("***** 字节码校验通过, class=%s, method=%s *****\n",
                        asmClass.getClassNode().name, method.name);
            }catch (Exception ex) {
//                System.err.printf("***** 字节码校验不通过, class=%s, method=%s,err=%s *****\n", asmClass.getClassNode().name, method.name, ex.getMessage());
                System.err.printf("***** 字节码校验不通过, class=%s, method=%s,err=%s *****\n", asmClass.getClassNode().name, method.name, ExceptionUtil.getExceptionTracing(ex));
                System.exit(0);
            }
        }

    }


    private void checkMethod(List<URL> dependentLib,MyAssemblyClass asmClass, MyAssemblyMethod myAssemblyMethod ) {
        MethodNode methodNode = myAssemblyMethod.getMethodNode();

        /**
         * 先输出到文本
         */
        //指令
        MyInstructionInfo instructionInfo = myAssemblyMethod.buildInstructionInfo();
        Map<LabelNode, Integer> labelIndexMap = instructionInfo.getLabelIndexMap();

        String assemblyCode = instructionInfo.getAssemblyCode();

        //行号
        List<MyLineNumber> markLines = instructionInfo.getMarkLines();
        List<int[]> lineTable = new ArrayList<>();   //模拟行号表组件
        for(MyLineNumber n : markLines) {
            lineTable.add(new int[]{n.getLabelIndex(),n.getLineSource(),n.getLineEditor()});
        }

        //本地变量表
        List<LocalVariableNode> localVariables = methodNode.localVariables;
        List<String[]> localVariableTable = new ArrayList<>();   //模拟本地变量表组件
        if(null != localVariables) {
            for(LocalVariableNode n : localVariables) {
                Integer start = labelIndexMap.get(n.start);
                Integer end = labelIndexMap.get(n.end);
                localVariableTable.add(new String[]{"L"+start,"L"+end,n.name,n.desc});
            }
        }


        //异常表
        List<TryCatchBlockNode> tryCatchBlockNodes = methodNode.tryCatchBlocks;
        List<String[]> tryCatchBlockNodeTable = new ArrayList<>();   //模拟异常表组件
        if(null != tryCatchBlockNodes) {
            for(TryCatchBlockNode n : tryCatchBlockNodes) {
                Integer start = labelIndexMap.get(n.start);
                Integer end = labelIndexMap.get(n.end);
                Integer handler = labelIndexMap.get(n.handler);
                tryCatchBlockNodeTable.add(new String[]{"L"+start,"L"+end,"L"+handler,n.type});
            }
        }




        /**
         * 再从文本/组件读入字节码
         */
        String[] assemblyCodes = assemblyCode.split("\n");

        List<AbstractInsnNode> insnList = new ArrayList<>();

        //先构建LabelNode
        Map<String,LabelNode> labelNodeMap = new HashMap<>();
        TreeMap<Integer, MyLineNumber> lineEditorMap = new TreeMap<>();
        for(int[] line : lineTable) {
            LabelNode labelNode = new LabelNode();
            labelNodeMap.put("L"+line[0],labelNode);

            MyLineNumber myLineNumber = new MyLineNumber();
            myLineNumber.setLabelIndex(line[0]);
            myLineNumber.setLineSource(line[1]);
            myLineNumber.setLineEditor(line[2]);
            lineEditorMap.put(line[2],myLineNumber);
        }

        //构建局部变量
        List<LocalVariableNode> newLocalVariables = new ArrayList<>();
        int count = 0;
        for(String[] line : localVariableTable) {
            LabelNode start = labelNodeMap.get(line[0]);
            LabelNode end = labelNodeMap.get(line[1]);
            String name = line[2];
            String desc = line[3];

            LocalVariableNode localVariableNode = new LocalVariableNode(name,desc,null,start,end,count++);
            newLocalVariables.add(localVariableNode);
        }
        methodNode.localVariables = newLocalVariables;

        //构建Exception Table
        List<TryCatchBlockNode> newTryCatchBlocks = new ArrayList<>();
        for(String[] line : tryCatchBlockNodeTable) {
            LabelNode start = labelNodeMap.get(line[0]);
            LabelNode end = labelNodeMap.get(line[1]);
            LabelNode jump = labelNodeMap.get(line[2]);
            String type = line[3];
            TryCatchBlockNode tryCatchBlockNode = new TryCatchBlockNode(start,end,jump,type);
            newTryCatchBlocks.add(tryCatchBlockNode);
        }
        if(!newTryCatchBlocks.isEmpty()) {
            methodNode.tryCatchBlocks = newTryCatchBlocks;
        }


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
                    AbstractInsnNode insnNode = myAssemblyMethod.parseInstNode(labelNodeMap,firstInstnType, args);
                    insnList.add(insnNode);
                }
            }else {
                //多行指令构建
                multiLineInstn.addLine(line);
            }
        }


        if(!lineEditorMap.isEmpty()
                && assemblyCodes.length-1 < lineEditorMap.lastKey()) {
            insnList.add( labelNodeMap.get("L"+ lineEditorMap.lastEntry().getValue().getLabelIndex()));
        }

        InsnList instNodes = new InsnList();
        insnList.forEach(instNodes::add);

        methodNode.instructions = instNodes;

        /**
         * 进行字节码校验
         */
        MyClassWriter classWriter = new MyClassWriter(dependentLib,ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        CheckClassAdapter checkAdapter = new CheckClassAdapter(classWriter, true);
        asmClass.getClassNode().accept(checkAdapter);
    }




}
