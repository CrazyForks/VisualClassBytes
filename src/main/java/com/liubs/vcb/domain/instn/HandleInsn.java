package com.liubs.vcb.domain.instn;

import org.objectweb.asm.Handle;

/**
 * @author Liubsyy
 * @date 2024/11/6
 */
public class HandleInsn {
    private Handle asmHandle;

    public static HandleInsn create(String line){
        String[] split = line.trim().split("[ ]");
        String ownerAndName = split[0];
        String[] ownerAndNameSplit = ownerAndName.split("[.]");
        String owner = ownerAndNameSplit[0];
        String name = ownerAndNameSplit[1];
        String desc = split[1];
        String tag = split[2];
        boolean isInterface = split.length > 3 && "itf".equals(split[3]);

        Handle handle = new Handle(Integer.parseInt(tag),owner,name,desc,isInterface);

        HandleInsn handleInsn = new HandleInsn(handle);
        return handleInsn;
    }

    public HandleInsn(Handle asmHandle) {
        this.asmHandle = asmHandle;
    }


    public Handle getAsmHandle() {
        return asmHandle;
    }

    public String makeInstString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(asmHandle.getOwner())
                .append(".")
                .append(asmHandle.getName())
                .append(" ")
                .append(asmHandle.getDesc())
                .append(" ")
                .append(asmHandle.getTag())
                .append(asmHandle.isInterface() ? " itf" : "");
       return strBuilder.toString();
    }
}
