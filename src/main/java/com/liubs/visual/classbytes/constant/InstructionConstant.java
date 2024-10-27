package com.liubs.visual.classbytes.constant;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.util.Printer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liubsyy
 * @date 2024/10/27
 */
public class InstructionConstant {

    public static final Map<Integer,Integer> OPCODE_TO_INSTN = new HashMap<>();
    public static final Map<String,Integer> NAME_TO_OPCODE = new HashMap<>();

    static {
        //insn
        OPCODE_TO_INSTN.put(Opcodes.NOP, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ACONST_NULL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ICONST_M1, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ICONST_0, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ICONST_1, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ICONST_2, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ICONST_3, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ICONST_4, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ICONST_5, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LCONST_0, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LCONST_1, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FCONST_0, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FCONST_1, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FCONST_2, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DCONST_0, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DCONST_1, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.AALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.BALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.CALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.SALOAD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.AASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.BASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.CASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.SASTORE, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.POP, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.POP2, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DUP, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DUP_X1, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DUP_X2, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DUP2, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DUP2_X1, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DUP2_X2, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.SWAP, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IADD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LADD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FADD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DADD, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ISUB, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LSUB, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FSUB, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DSUB, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IMUL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LMUL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FMUL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DMUL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IDIV, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LDIV, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FDIV, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DDIV, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IREM, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LREM, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FREM, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DREM, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.INEG, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LNEG, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FNEG, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DNEG, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ISHL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LSHL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ISHR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LSHR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IUSHR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LUSHR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IAND, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LAND, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IOR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LOR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IXOR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LXOR, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.I2L, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.I2F, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.I2D, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.L2I, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.L2F, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.L2D, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.F2I, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.F2L, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.F2D, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.D2I, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.D2L, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.D2F, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.I2B, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.I2C, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.I2S, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LCMP, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FCMPL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FCMPG, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DCMPL, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DCMPG, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.IRETURN, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.LRETURN, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.FRETURN, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.DRETURN, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ARETURN, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.RETURN, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ARRAYLENGTH, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.ATHROW, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.MONITORENTER, AbstractInsnNode.INSN);
        OPCODE_TO_INSTN.put(Opcodes.MONITOREXIT, AbstractInsnNode.INSN);

        //INT_INSN
        OPCODE_TO_INSTN.put(Opcodes.BIPUSH , AbstractInsnNode.INT_INSN);
        OPCODE_TO_INSTN.put(Opcodes.SIPUSH , AbstractInsnNode.INT_INSN);
        OPCODE_TO_INSTN.put(Opcodes.NEWARRAY , AbstractInsnNode.INT_INSN);

        //VAR_INSN
        OPCODE_TO_INSTN.put(Opcodes.ILOAD, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.LLOAD, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.FLOAD, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.DLOAD, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.ALOAD, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.ISTORE, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.LSTORE, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.FSTORE, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.DSTORE, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.ASTORE, AbstractInsnNode.VAR_INSN);
        OPCODE_TO_INSTN.put(Opcodes.RET, AbstractInsnNode.VAR_INSN);


        //TYPE_INSN
        OPCODE_TO_INSTN.put(Opcodes.NEW, AbstractInsnNode.TYPE_INSN);
        OPCODE_TO_INSTN.put(Opcodes.ANEWARRAY, AbstractInsnNode.TYPE_INSN);
        OPCODE_TO_INSTN.put(Opcodes.CHECKCAST, AbstractInsnNode.TYPE_INSN);
        OPCODE_TO_INSTN.put(Opcodes.INSTANCEOF, AbstractInsnNode.TYPE_INSN);

        //FIELD_INSN
        OPCODE_TO_INSTN.put(Opcodes.GETSTATIC, AbstractInsnNode.FIELD_INSN);
        OPCODE_TO_INSTN.put(Opcodes.PUTSTATIC, AbstractInsnNode.FIELD_INSN);
        OPCODE_TO_INSTN.put(Opcodes.GETFIELD, AbstractInsnNode.FIELD_INSN);
        OPCODE_TO_INSTN.put(Opcodes.PUTFIELD, AbstractInsnNode.FIELD_INSN);

        //METHOD_INSN
        OPCODE_TO_INSTN.put(Opcodes.INVOKEVIRTUAL, AbstractInsnNode.METHOD_INSN);
        OPCODE_TO_INSTN.put(Opcodes.INVOKESPECIAL, AbstractInsnNode.METHOD_INSN);
        OPCODE_TO_INSTN.put(Opcodes.INVOKESTATIC, AbstractInsnNode.METHOD_INSN);
        OPCODE_TO_INSTN.put(Opcodes.INVOKEINTERFACE, AbstractInsnNode.METHOD_INSN);

        //INVOKE_DYNAMIC_INSN
        OPCODE_TO_INSTN.put(Opcodes.INVOKEDYNAMIC, AbstractInsnNode.INVOKE_DYNAMIC_INSN);

        //JUMP_INSN
        OPCODE_TO_INSTN.put(Opcodes.IFEQ, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IFNE, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IFLT, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IFGE, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IFGT, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IFLE, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ICMPEQ, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ICMPNE, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ICMPLT, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ICMPGE, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ICMPGT, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ICMPLE, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ACMPEQ, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IF_ACMPNE, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.GOTO, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.JSR, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IFNULL, AbstractInsnNode.JUMP_INSN);
        OPCODE_TO_INSTN.put(Opcodes.IFNONNULL, AbstractInsnNode.JUMP_INSN);


        OPCODE_TO_INSTN.put( Opcodes.LDC, AbstractInsnNode.LDC_INSN);
        OPCODE_TO_INSTN.put( Opcodes.IINC, AbstractInsnNode.IINC_INSN);
        OPCODE_TO_INSTN.put( Opcodes.TABLESWITCH, AbstractInsnNode.TABLESWITCH_INSN);
        OPCODE_TO_INSTN.put( Opcodes.LOOKUPSWITCH, AbstractInsnNode.LOOKUPSWITCH_INSN);
        OPCODE_TO_INSTN.put( Opcodes.MULTIANEWARRAY, AbstractInsnNode.MULTIANEWARRAY_INSN);


        //OPCode name
        for(int i = 0,len=Printer.OPCODES.length ;i<len;i++) {
            NAME_TO_OPCODE.put(Printer.OPCODES[i].toLowerCase(),i);
        }
    }
}
