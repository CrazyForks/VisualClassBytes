package com.liubs.visual.classbytes.aggregate;

import com.liubs.visual.classbytes.constant.InstructionConstant;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多行指令：tableswitch, lookupswitch
 * @author Liubsyy
 * @date 2024/10/27
 */
public class MultiLineInstn {

    //多行指令集合
    private List<String> multiLine = new ArrayList<>();

    public static MultiLineInstn newInstn(){
        return new MultiLineInstn();
    }

    public void addLine(String line){
        multiLine.add(line);
    }

    public static boolean isMultiLineInstn(String instn){
        return "tableswitch".equals(instn) || "lookupswitch".equals(instn);
    }

    public boolean isMultiEnd(String instn){
        return InstructionConstant.NAME_TO_OPCODE.containsKey(instn);
    }

    public AbstractInsnNode parseInstnNode(Map<String, LabelNode> labelNodeMap){
        String[] fistLine = multiLine.get(0).split("\\s");
        String instName = fistLine[0].trim();
        if("tableswitch".equals(instName)) {
            int min = Integer.parseInt(fistLine[1]);
            int max = Integer.parseInt(fistLine[3]);
            LabelNode deft = null;
            List<LabelNode> labelNodes = new ArrayList<>();
            for(int i = 1;i<multiLine.size();i++) {
                String[] split = multiLine.get(i).split(":");
                String key = split[0].trim();
                String value = split[1].trim();
                if("default".equals(key)){
                    deft = labelNodeMap.get(value);
                } else {
                    labelNodes.add(labelNodeMap.get(value));
                }
            }
            TableSwitchInsnNode tableSwitchInsnNode = new TableSwitchInsnNode(min,max,deft,labelNodes.toArray(new LabelNode[0]));
            return tableSwitchInsnNode;
        }else if("lookupswitch".equals(instName)) {
            LabelNode deft = null;
            List<Integer> keys = new ArrayList<>();
            List<LabelNode> labelNodes = new ArrayList<>();
            for(int i = 1;i<multiLine.size();i++) {
                String[] split = multiLine.get(i).split(":");
                String key = split[0].trim();
                String value = split[1].trim();
                if("default".equals(key)){
                    deft = labelNodeMap.get(value);
                } else {
                    keys.add(Integer.parseInt(key));
                    labelNodes.add(labelNodeMap.get(value));
                }
            }
            LookupSwitchInsnNode lookupSwitchInsnNode = new LookupSwitchInsnNode(deft,
                    keys.stream().mapToInt(Integer::intValue).toArray(),
                    labelNodes.toArray(new LabelNode[0]));

            return lookupSwitchInsnNode;
        }
        return null;
    }

}
