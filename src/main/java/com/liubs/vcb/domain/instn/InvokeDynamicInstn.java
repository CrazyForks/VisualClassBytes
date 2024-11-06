package com.liubs.vcb.domain.instn;

import com.liubs.vcb.ex.InstructionException;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.util.Printer;


/**
 * invokedynamic 指令
 * @author Liubsyy
 * @date 2024/11/4
 */
public class InvokeDynamicInstn {

    public static String parseString(InvokeDynamicInsnNode insnNode){
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(Printer.OPCODES[insnNode.getOpcode()].toLowerCase()).append(" ");

        //name
        strBuilder.append(insnNode.name).append(" ");

        //desc
        strBuilder.append(insnNode.desc).append(" ");

        //bsm
        strBuilder.append("bsm{");
        HandleInsn bsm = new HandleInsn(insnNode.bsm);
        strBuilder.append(bsm.makeInstString());
        strBuilder.append("}");

        //args
        strBuilder.append("args{");
        for(int i = 0,len=insnNode.bsmArgs.length; i< len ;i++){
            Object arg = insnNode.bsmArgs[i];
            if(arg instanceof Handle) {
                HandleInsn handleInsn = new HandleInsn((Handle)arg);
                strBuilder.append("handle{");
                strBuilder.append(handleInsn.makeInstString());
                strBuilder.append("}");
            }else if(arg instanceof Type) {
                Type type = (Type)arg;
                strBuilder.append(type.getDescriptor());
            }
            if(i<len-1) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("}");
        return strBuilder.toString();
    }


    public static InvokeDynamicInsnNode parseInsn(String line){
        String name;
        String desc;
        Handle bsm;
        Object[] bsmArgs;

        line = line.trim();
        int begin, end;

        //name
        begin=0;
        end = line.indexOf(" ",begin);
        name = line.substring(begin,end);

        //desc
        begin = end+1;
        end = line.indexOf(" ",begin);
        desc = line.substring(begin,end);

        //bsm
        begin = line.indexOf("bsm{",end);
        if(begin<0) {
            throw new InstructionException("Missing bsm(Bootstrap Method) in invokedynamic");
        }
        end = line.indexOf("}",begin);
        if(end<0) {
            throw new InstructionException("Missing '}' in invokedynamic");
        }
        String bspStr = line.substring(begin+4,end);
        bsm = HandleInsn.create(bspStr).getAsmHandle();

        //args
        begin = line.indexOf("args{",end);
        if(begin<0) {
            throw new InstructionException("Missing args in invokedynamic");
        }
        end = line.lastIndexOf("}");
        if(end<0) {
            throw new InstructionException("Missing '}' of args in invokedynamic");
        }
        String[] args = line.substring(begin+5,end).trim().split(",");

        bsmArgs = new Object[args.length];
        for(int i = 0,len=args.length ;i<len ;i++) {
            String arg = args[i];
            if(arg.startsWith("handle{") && arg.endsWith("}")) {
                arg = arg.substring("handle{".length(), arg.length() - 1);
                bsmArgs[i] = HandleInsn.create(arg).getAsmHandle();
            }else {
                Type type = Type.getType(arg);
                bsmArgs[i] = type;
            }
        }

        InvokeDynamicInsnNode insnNode = new InvokeDynamicInsnNode(name,desc,bsm,bsmArgs);
        return insnNode;
    }
}
