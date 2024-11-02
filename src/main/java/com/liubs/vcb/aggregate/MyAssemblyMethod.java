package com.liubs.vcb.aggregate;

import com.liubs.vcb.constant.InstructionConstant;
import com.liubs.vcb.entity.MyInstructionInfo;
import com.liubs.vcb.entity.MyLineNumber;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;

import java.util.*;

/**
 * @author Liubsyy
 * @date 2024/10/22
 */
public class MyAssemblyMethod {
    private MethodNode methodNode;

    public MyAssemblyMethod(MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    public String name(){
        return methodNode.name;
    }

    public Map<LabelNode,Integer> buildLabelIndex(){
        ListIterator<?> instructions = methodNode.instructions.iterator();
        Map<LabelNode,Integer> labelIndexMap = new HashMap<>();
        while (instructions.hasNext()) {
            Object c = instructions.next();
            if(c instanceof LabelNode) {
                labelIndexMap.computeIfAbsent((LabelNode) c, key -> labelIndexMap.size());
            }
        }
        return labelIndexMap;
    }


    public MethodNode getMethodNode() {
        return methodNode;
    }


    public MyInstructionInfo buildInstructionInfo(){
        int lineCounter = 0;
        StringBuilder assmblyBuild = new StringBuilder();
        List<MyLineNumber> markLines = new ArrayList<>();
        List<FrameNode> frameNodes = new ArrayList<>();

        /** 先遍历LabelNode构建索引编号*/
        Map<LabelNode,Integer> labelIndexMap = buildLabelIndex();

        /**
         * 遍历字节码指令信息，包括指令文本，行号信息，FrameNode信息
         * */
        ListIterator<?> instructions = methodNode.instructions.iterator();
        while (instructions.hasNext()) {
            Object currentInsn = instructions.next();

            if (currentInsn instanceof InsnNode) {
                InsnNode insn = (InsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[insn.getOpcode()].toLowerCase());
            } else if (currentInsn instanceof FrameNode) {
                frameNodes.add((FrameNode)currentInsn );
                continue;
            } else if (currentInsn instanceof LabelNode) {

                LabelNode label = (LabelNode) currentInsn;
                MyLineNumber lineNumber = new MyLineNumber();
                lineNumber.setLineEditor(lineCounter);
                lineNumber.setLabelIndex(labelIndexMap.get(label));
                markLines.add(lineNumber);
                continue;
            } else if (currentInsn instanceof IntInsnNode) {
                IntInsnNode intInsn = (IntInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[intInsn.getOpcode()].toLowerCase()+" "+intInsn.operand);
            } else if (currentInsn instanceof LdcInsnNode) {
                LdcInsnNode ldcInsn = (LdcInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[ldcInsn.getOpcode()].toLowerCase()).append(" ");
                if (ldcInsn.cst instanceof String) {
                    Printer.appendString(assmblyBuild, (String) ldcInsn.cst);
                } else if (ldcInsn.cst instanceof Type) {
                    assmblyBuild.append(((Type) ldcInsn.cst).getDescriptor()).append(".class");
                } else {
                    assmblyBuild.append(ldcInsn.cst);
                }

            } else if (currentInsn instanceof VarInsnNode) {
                VarInsnNode varInsn = (VarInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[varInsn.getOpcode()].toLowerCase()+" "+varInsn.var);
            } else if (currentInsn instanceof IincInsnNode) {
                IincInsnNode iincInsn = (IincInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[iincInsn.getOpcode()].toLowerCase()+" "+iincInsn.var+" "+iincInsn.incr);
            } else if (currentInsn instanceof JumpInsnNode) {
                JumpInsnNode jumpInsn = (JumpInsnNode) currentInsn;
                Integer index = labelIndexMap.get(jumpInsn.label);
                assmblyBuild.append(Printer.OPCODES[jumpInsn.getOpcode()].toLowerCase()+" L"+index);
            } else if (currentInsn instanceof TypeInsnNode) {
                TypeInsnNode typeInsn = (TypeInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[typeInsn.getOpcode()].toLowerCase()+" "+typeInsn.desc);
            } else if (currentInsn instanceof FieldInsnNode) {
                FieldInsnNode fieldInsn = (FieldInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[fieldInsn.getOpcode()].toLowerCase() +" "+ fieldInsn.owner+"."+fieldInsn.name+" "+fieldInsn.desc);
            } else if (currentInsn instanceof LineNumberNode) {
                LineNumberNode lineNumberInsn = (LineNumberNode) currentInsn;
                for(int i = markLines.size()-1 ; i>=0 ;i--){
                    MyLineNumber lineNumber = markLines.get(i);
                    if(lineNumber.getLabelIndex() == labelIndexMap.get(lineNumberInsn.start)) {
                        lineNumber.setLineSource(lineNumberInsn.line);
                        break;
                    }
                }
                continue;
            } else if (currentInsn instanceof MethodInsnNode) {
                MethodInsnNode methodInsn = (MethodInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[methodInsn.getOpcode()].toLowerCase()).append(" ").append(methodInsn.owner).append(".").append(methodInsn.name).append(" ").append(methodInsn.desc);
            } else if (currentInsn instanceof TableSwitchInsnNode) {
                TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[tableSwitchInsn.getOpcode()].toLowerCase()).append(" ").append(tableSwitchInsn.min).append(" to ").append(tableSwitchInsn.max).append("\n");
                lineCounter++;
                for(int i = 0,len=tableSwitchInsn.labels.size(); i < len; i++) {
                    assmblyBuild.append("          ").append(tableSwitchInsn.min + i).append(": ");
                    assmblyBuild.append("L").append(labelIndexMap.get(tableSwitchInsn.labels.get(i)));
                    assmblyBuild.append('\n');
                    lineCounter++;
                }
                assmblyBuild.append("          default: L").append(labelIndexMap.get(tableSwitchInsn.dflt));

            } else if (currentInsn instanceof LookupSwitchInsnNode) {
                LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[lookupSwitchInsn.getOpcode()].toLowerCase()).append("\n");
                lineCounter++;
                for(int i = 0,len=lookupSwitchInsn.labels.size(); i < len; i++) {
                    assmblyBuild.append("          ").append(lookupSwitchInsn.keys.get(i)).append(": ");
                    assmblyBuild.append("L").append(labelIndexMap.get(lookupSwitchInsn.labels.get(i)));
                    assmblyBuild.append('\n');
                    lineCounter++;
                }
                assmblyBuild.append("          default: L").append(labelIndexMap.get(lookupSwitchInsn.dflt));

            } else if (currentInsn instanceof InvokeDynamicInsnNode) {
                InvokeDynamicInsnNode invokeDynamicInsn = (InvokeDynamicInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[invokeDynamicInsn.getOpcode()].toLowerCase().toLowerCase()).append(" ")
                        .append(invokeDynamicInsn.name).append(" ").append(invokeDynamicInsn.desc)
                        .append(" handle{").append(invokeDynamicInsn.bsm.getOwner()).append(".").append(invokeDynamicInsn.bsm.getName()).append(" ").append(invokeDynamicInsn.bsm.getDesc()).append("}")
                        .append(" args{").append(Arrays.toString(invokeDynamicInsn.bsmArgs)).append("}");
            } else if (currentInsn instanceof MultiANewArrayInsnNode) {
                MultiANewArrayInsnNode multiANewArrayInsn = (MultiANewArrayInsnNode) currentInsn;
                assmblyBuild.append(Printer.OPCODES[multiANewArrayInsn.getOpcode()].toLowerCase().toLowerCase()).append(" ").append(multiANewArrayInsn.desc).append(" ").append(multiANewArrayInsn.dims);
            }

            assmblyBuild.append("\n");
            lineCounter++;
        }


        MyInstructionInfo myInstructionInfo = new MyInstructionInfo();
        myInstructionInfo.setAssemblyCode(assmblyBuild.toString());
        myInstructionInfo.setMarkLines(markLines);
        myInstructionInfo.setFrameNodes(frameNodes);
        myInstructionInfo.setLabelIndexMap(labelIndexMap);

        return myInstructionInfo;
    }


    public AbstractInsnNode parseInstNode(Map<String,LabelNode> labelNodeMap,String instn, String... args) {
        Integer opCode = InstructionConstant.NAME_TO_OPCODE.get(instn);
        if(null == opCode){
            throw new IllegalArgumentException("Unknown instn: " + instn);
        }
        Integer instType = InstructionConstant.OPCODE_TO_INSTN.get(opCode);
        if (instType == AbstractInsnNode.INSN) {
            return new InsnNode(opCode);
        } else if (instType == AbstractInsnNode.INT_INSN) {
           return new IntInsnNode(opCode, Integer.parseInt(args[0]));
        } else if (instType == AbstractInsnNode.VAR_INSN) {
           return new VarInsnNode(opCode,Integer.parseInt(args[0]));
        } else if (instType == AbstractInsnNode.TYPE_INSN) {
           return new TypeInsnNode(opCode,args[0]);
        } else if (instType == AbstractInsnNode.FIELD_INSN) {
            String[] split = args[0].split("[.]");
            return new FieldInsnNode(opCode,split[0],split[1],args[1]);
        } else if (instType == AbstractInsnNode.METHOD_INSN) {
            String[] split = args[0].split("[.]");
            return new MethodInsnNode(opCode,split[0],split[1],args[1]);
        } else if (instType == AbstractInsnNode.INVOKE_DYNAMIC_INSN) {

        } else if (instType == AbstractInsnNode.JUMP_INSN) {
           return new JumpInsnNode(opCode,labelNodeMap.get(args[0]));
        } else if (instType == AbstractInsnNode.LDC_INSN) {
            String s = args[0];
            if(s.length()>1 && s.charAt(0) == '\"' && s.charAt(s.length()-1)=='\"') {
                s = s.substring(1,s.length()-1);
            }
            return new LdcInsnNode(s);    //TODO 类型判断
        } else if (instType == AbstractInsnNode.IINC_INSN) {
            return new IincInsnNode(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        } else if (instType == AbstractInsnNode.TABLESWITCH_INSN) {
            //在MultiLineInstn中处理
        } else if (instType == AbstractInsnNode.LOOKUPSWITCH_INSN) {
            //在MultiLineInstn中处理
        } else if (instType == AbstractInsnNode.MULTIANEWARRAY_INSN) {
           return new MultiANewArrayInsnNode(args[0],Integer.parseInt(args[1]));
        }

        throw new IllegalArgumentException("Unknown instruction type: " + instType);

    }


}
