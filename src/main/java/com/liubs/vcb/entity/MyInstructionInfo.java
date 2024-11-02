package com.liubs.vcb.entity;

import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.List;
import java.util.Map;

/**
 * @author Liubsyy
 * @date 2024/10/21
 */
public class MyInstructionInfo {

    //字节码指令
    private String assemblyCode;

    //行号信息
    private List<MyLineNumber> markLines ;

    private List<FrameNode> frameNodes ;

    //label编号索引
    Map<LabelNode,Integer> labelIndexMap;

    public String getAssemblyCode() {
        return assemblyCode;
    }

    public void setAssemblyCode(String assemblyCode) {
        this.assemblyCode = assemblyCode;
    }

    public List<MyLineNumber> getMarkLines() {
        return markLines;
    }

    public void setMarkLines(List<MyLineNumber> markLines) {
        this.markLines = markLines;
    }

    public List<FrameNode> getFrameNodes() {
        return frameNodes;
    }

    public void setFrameNodes(List<FrameNode> frameNodes) {
        this.frameNodes = frameNodes;
    }

    public Map<LabelNode, Integer> getLabelIndexMap() {
        return labelIndexMap;
    }

    public void setLabelIndexMap(Map<LabelNode, Integer> labelIndexMap) {
        this.labelIndexMap = labelIndexMap;
    }
}
