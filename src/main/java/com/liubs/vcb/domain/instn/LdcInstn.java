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
        if(line.length()>=2 && line.startsWith("\"") && line.endsWith("\"")) {
            cst = StringUtils.unescape(line.substring(1,line.length()-1));
        }else if (line.matches("^[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?F$")){
            cst = Float.parseFloat(line.substring(0,line.length()-1));
        }else if(line.matches("^[+-]?\\d+L$")){
            cst = Long.parseLong(line.substring(0,line.length()-1));
        }else if (line.matches("^[+-]?\\d+(\\.\\d+)?([eE][+-]?\\d+)?D$")){
            cst = Double.parseDouble(line.substring(0,line.length()-1));
        }else if(line.startsWith("handle{") && line.endsWith("}")){
            cst = HandleInsn.create(line.substring(7,line.length()-1)).getAsmHandle();
        }else if(line.startsWith("type{") && line.endsWith("}")){
            cst = Type.getType(line.substring(5,line.length()-1));
        }else if(StringUtils.isNumeric(line)){
            cst = Integer.parseInt(line);
        }else {
            if("Infinity".equals(line) || "InfinityD".equals(line)){
                cst = Double.POSITIVE_INFINITY;
            }else if("-Infinity".equals(line) || "-InfinityD".equals(line)){
                cst = Double.NEGATIVE_INFINITY;
            }else if("NaN".equals(line)|| "NaND".equals(line)){
                cst = Double.NaN;
            }else if("InfinityF".equals(line)){
                cst = Float.POSITIVE_INFINITY;
            }else if("-InfinityF".equals(line)){
                cst = Float.NEGATIVE_INFINITY;
            }else if("NaNF".equals(line)){
                cst = Float.NaN;
            }else{
                throw new InstructionException("Unknow ldc: "+line);
            }
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
            return "\""+StringUtils.escape(cst.toString())+"\"";
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
