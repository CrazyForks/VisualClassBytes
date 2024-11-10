package com.liubs.vcb.constant;


/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class ConstantPoolConst {
    private static final String[] NAMES = {"", "Utf8", "", "Integer", "Float", "Long", "Double",
            "Class", "String", "Fieldref", "Methodref", "InterfaceMethodref", "NameAndType", "", "",
            "MethodHandle", "MethodType", "Dynamic", "InvokeDynamic", "Module", "Package"};


    public static String getConstantName(final int index) {
        return NAMES[index];
    }


}
