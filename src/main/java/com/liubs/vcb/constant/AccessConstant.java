package com.liubs.vcb.constant;

import java.util.*;

/**
 * @author Liubsyy
 * @date 2024/10/22
 *
 * @see org.objectweb.asm.Opcodes
 */
public class AccessConstant {
    // Map for class flags
    public static final Map<Integer, String> CLASS_FLAGS = new LinkedHashMap<>();

    // Map for method flags
    public static final Map<Integer, String> METHOD_FLAGS = new LinkedHashMap<>();

    // Map for field flags
    public static final Map<Integer, String> FIELD_FLAGS = new LinkedHashMap<>();

    static {
        CLASS_FLAGS.put(0x0001, "public");
        CLASS_FLAGS.put(0x0002, "private");
        CLASS_FLAGS.put(0x0004, "protected");
        CLASS_FLAGS.put(0x0008, "static");
        CLASS_FLAGS.put(0x0010, "final");
//        CLASS_FLAGS.put(0x0020, "super");
        CLASS_FLAGS.put(0x0200, "interface");
        CLASS_FLAGS.put(0x0400, "abstract");
        CLASS_FLAGS.put(0x1000, "synthetic");
        CLASS_FLAGS.put(0x2000, "annotation");
        CLASS_FLAGS.put(0x4000, "enum");
        CLASS_FLAGS.put(0x8000, "module");

        METHOD_FLAGS.put(0x0001, "public");
        METHOD_FLAGS.put(0x0002, "private");
        METHOD_FLAGS.put(0x0004, "protected");
        METHOD_FLAGS.put(0x0008, "static");
        METHOD_FLAGS.put(0x0010, "final");
        METHOD_FLAGS.put(0x0020, "synchronized");
        METHOD_FLAGS.put(0x0040, "bridge");
        METHOD_FLAGS.put(0x0080, "varargs");
        METHOD_FLAGS.put(0x0100, "native");
        METHOD_FLAGS.put(0x0400, "abstract");
        METHOD_FLAGS.put(0x0800, "strict");
        METHOD_FLAGS.put(0x1000, "synthetic");
        METHOD_FLAGS.put(0x8000, "mandated");

        FIELD_FLAGS.put(0x0001, "public");
        FIELD_FLAGS.put(0x0002, "private");
        FIELD_FLAGS.put(0x0004, "protected");
        FIELD_FLAGS.put(0x0008, "static");
        FIELD_FLAGS.put(0x0010, "final");
        FIELD_FLAGS.put(0x0040, "volatile");
        FIELD_FLAGS.put(0x0080, "transient");
        FIELD_FLAGS.put(0x1000, "synthetic");
        FIELD_FLAGS.put(0x8000, "mandated");
    }


    public static List<String> getMethodFlagNames(int accessFlag) {
        List<String> flagNames = new ArrayList<>();

        // Iterate through all flags in the methodFlags map
        for (Map.Entry<Integer, String> entry : METHOD_FLAGS.entrySet()) {
            int flag = entry.getKey();
            // Check if the current flag is set in the accessFlag using bitwise AND
            if ((accessFlag & flag) != 0) {
                flagNames.add(entry.getValue());
            }
        }
        return flagNames;
    }

    public static List<String> getFieldsFlagNames(int accessFlag) {
        List<String> flagNames = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : FIELD_FLAGS.entrySet()) {
            int flag = entry.getKey();
            if ((accessFlag & flag) != 0) {
                flagNames.add(entry.getValue());
            }
        }
        return flagNames;
    }

    public static List<String> getClassFlagNames(int accessFlag) {
        List<String> flagNames = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : CLASS_FLAGS.entrySet()) {
            int flag = entry.getKey();
            if ((accessFlag & flag) != 0) {
                flagNames.add(entry.getValue());
            }
        }
        return flagNames;
    }

    public static List<String> toDescriptions(Map<Integer, String> flags){
        List<String> flagNames = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : flags.entrySet()) {
            flagNames.add(String.format("0x%04x(%s)", entry.getKey(),entry.getValue()));
        }
        return flagNames;
    }

    public static List<Integer> splitAccess(int accessFlag){
        List<Integer> values = new ArrayList<>();

        int bitValue = 1;
        while (accessFlag > 0) {
            if ((accessFlag & 1) == 1) { // 检查当前位是否为1
                values.add(bitValue);
            }
            accessFlag >>= 1; // 右移一位
            bitValue <<= 1; // 下一个2的幂
        }

        return values;
    }




}
