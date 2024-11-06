package com.liubs.vcb.domain.instn;

import com.liubs.vcb.ex.InstructionException;
import com.liubs.vcb.util.StringUtils;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * @author Liubsyy
 * @date 2024/11/6
 */
public class LdcInstn {
    private LdcInsnNode ldcInsnNode;

    public static LdcInstn create(String line){
        Object cst = null;
        if(line.length()>2 && line.startsWith("\"") && line.endsWith("\"")) {
            cst = line.substring(1,line.length()-1);
        }else if(line.matches("\\b[+-]?\\d+(\\.\\d+)?F\\b")){
            cst = Float.parseFloat(line.substring(0,line.length()-1));
        }else if(line.matches("\\b[+-]?\\d+L\\b")){
            cst = Long.parseLong(line.substring(0,line.length()-1));
        }else if(line.matches("\\b[+-]?\\d+(\\.\\d+)?D\\b")){
            cst = Double.parseDouble(line.substring(0,line.length()-1));
        }else if(line.startsWith("handle{") && line.endsWith("}")){
            cst = HandleInsn.create(line).getAsmHandle();
        }else if(line.startsWith("type{") && line.endsWith("}")){
            cst = Type.getType(line);
        }else if(StringUtils.isNumeric(line)){
            cst = Integer.parseInt(line);
        }else {
            throw new InstructionException("Unknow ldc: "+line);
        }

        return new LdcInstn(new LdcInsnNode(cst));
    }

    public LdcInsnNode getLdcInsnNode() {
        return ldcInsnNode;
    }

    public LdcInstn(LdcInsnNode ldcInsnNode) {
        this.ldcInsnNode = ldcInsnNode;
    }

    //ldc: int,float,long,double,String,MethodHandle,MethodType
    public String makeInstString(){
        Object cst = ldcInsnNode.cst;
        if(cst instanceof String){
            return "\""+cst+"\"";
        }else if(cst instanceof Float) {
            return cst+"F";
        }else if(cst instanceof Long){
            return cst+"L";
        }else if(cst instanceof Double){
            return cst+"D";
        }else if(cst instanceof Handle){
            return "handle{"+new HandleInsn((Handle) cst).makeInstString()+"}";
        }else if(cst instanceof Type){
            return "type{"+((Type) cst).getDescriptor()+"}";
        }else {
            //int
            return String.valueOf(cst);
        }
    }
}
