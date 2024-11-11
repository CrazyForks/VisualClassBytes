package com.liubs.vcb.domain.assemblycode;

import com.liubs.vcb.constant.ConstantPoolConst;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantObject;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;

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
        str.append("<");
        str.append(ConstantPoolConst.getConstantName(constant.getTag()));
        str.append("> ");

        if(constant.getTag() == Const.CONSTANT_Integer
                || constant.getTag() == Const.CONSTANT_Long
                || constant.getTag() == Const.CONSTANT_Float
                || constant.getTag() == Const.CONSTANT_Double) {
            if(constant instanceof ConstantObject) {
                try{
                    str.append(((ConstantObject)constant).getConstantValue(constantPool));
                }catch (Exception ex){}
            }
        }
        if(constant instanceof ConstantUtf8) {
            ConstantUtf8 constantUtf8 = (ConstantUtf8)constant;
            String bytes = constantUtf8.getBytes();
            if(bytes.length() > 20) {
                bytes = bytes.substring(0,20)+"...";
            }
            str.append(bytes);
        }


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
