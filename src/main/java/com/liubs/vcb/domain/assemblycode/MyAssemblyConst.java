package com.liubs.vcb.domain.assemblycode;

import com.liubs.vcb.constant.ConstantPoolConst;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantPool;

/**
 * @author Liubsyy
 * @date 2024/11/9
 */
public class MyAssemblyConst {
    private int index;
    private ConstantPool constantPool;
    private Constant constant;

    public MyAssemblyConst(int index,ConstantPool constantPool,Constant constant) {
        this.index = index;
        this.constantPool = constantPool;
        this.constant = constant;
    }

    public String getName(){
        StringBuilder str = new StringBuilder();
        str.append("[").append(index).append("] ");
        str.append(ConstantPoolConst.getConstantName(constant.getTag()));
//        str.append("_");
//        str.append(constant.toString());

        return str.toString();
    }

    public String getOverview(){
        return constant.toString();
    }

    public byte getTag(){
        return constant.getTag();
    }

    public <T extends Constant> T getConstant() {
        return (T)constant;
    }

    public void setConstant(Constant newConstant){
        constantPool.setConstant(index,newConstant);
        this.constant = newConstant;
    }

    public int getIndex() {
        return index;
    }
}
